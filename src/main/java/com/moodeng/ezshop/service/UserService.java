package com.moodeng.ezshop.service;

import com.moodeng.ezshop.auth.JwtTokenProvider;
import com.moodeng.ezshop.dto.request.LoginRequestDto;
import com.moodeng.ezshop.dto.request.SignupRequestDto;
import com.moodeng.ezshop.dto.response.LoginResponseDto;
import com.moodeng.ezshop.dto.service.LoginDetails;
import com.moodeng.ezshop.entity.User;
import com.moodeng.ezshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void signup(SignupRequestDto signupDto) {
        if (userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(signupDto.getPassword());

        User user = signupDto.toEntity(encodedPassword);

        userRepository.save(user);
    }

    @Transactional
    public LoginDetails login(LoginRequestDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        LoginResponseDto loginResponse = LoginResponseDto.of(user, accessToken);

        return new LoginDetails(loginResponse, refreshToken);
    }

    @Transactional
    public void logout(String accessToken, String refreshToken) {
        if(StringUtils.hasText(accessToken)) {
            Long remainingTime = jwtTokenProvider.getRemainingExpirationTimeFromAccessToken(accessToken);

            if (remainingTime > 0) {
                redisTemplate.opsForValue().set(
                        accessToken,
                        "logout",
                        remainingTime,
                        TimeUnit.MILLISECONDS
                );
            }
        }

        if(StringUtils.hasText(accessToken)) {
            Long remainingTime = jwtTokenProvider.getRemainingExpirationTimeFromRefreshToken(refreshToken);

            if (remainingTime > 0) {
                redisTemplate.opsForValue().set(
                        refreshToken,
                        "logout",
                        remainingTime,
                        TimeUnit.MILLISECONDS
                );
            }
        }
    }
}
