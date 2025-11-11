package com.moodeng.ezshop.service;

import com.moodeng.ezshop.auth.JwtTokenProvider;
import com.moodeng.ezshop.dto.request.LoginRequestDto;
import com.moodeng.ezshop.dto.request.ProfileUpdateRequestDto;
import com.moodeng.ezshop.dto.request.SignupRequestDto;
import com.moodeng.ezshop.dto.response.LoginResponseDto;
import com.moodeng.ezshop.dto.response.ProfileResponseDto;
import com.moodeng.ezshop.dto.response.ReissueResponseDto;
import com.moodeng.ezshop.dto.response.ResponseCode;
import com.moodeng.ezshop.dto.service.LoginDetails;
import com.moodeng.ezshop.entity.User;
import com.moodeng.ezshop.exception.BusinessLogicException;
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
            throw new BusinessLogicException(ResponseCode.DUPLICATED_EMAIL);
        }
        String encodedPassword = passwordEncoder.encode(signupDto.getPassword());

        User user = signupDto.toEntity(encodedPassword);

        userRepository.save(user);
    }

    @Transactional
    public LoginDetails login(LoginRequestDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new BusinessLogicException(ResponseCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BusinessLogicException(ResponseCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        LoginResponseDto loginResponse = LoginResponseDto.of(user, accessToken);

        return new LoginDetails(loginResponse, refreshToken);
    }

    @Transactional
    public void logout(String accessToken, String refreshToken) {
        if (StringUtils.hasText(accessToken)) {
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

        if (StringUtils.hasText(accessToken)) {
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

    @Transactional(readOnly = true)
    public ProfileResponseDto getProfileInfo(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ResponseCode.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."));

        return ProfileResponseDto.from(user);
    }

    @Transactional
    public void updateProfileInfo(String email, ProfileUpdateRequestDto updateDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ResponseCode.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."));


        if(StringUtils.hasText(updateDto.getName())) {
            user.setName(updateDto.getName());
        }
        if(StringUtils.hasText(updateDto.getPhone())) {
            user.setPhone(updateDto.getPhone());
        }
        if(StringUtils.hasText(updateDto.getAddress())) {
            user.setAddress(updateDto.getAddress());
        }
        if(StringUtils.hasText(updateDto.getNewPassword())) {
            user.setPassword(passwordEncoder.encode(updateDto.getNewPassword()));
        }
    }

    @Transactional
    public void signout(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ResponseCode.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."));

        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public ReissueResponseDto reissue(String refreshToken) {

        if(redisTemplate.hasKey(refreshToken)) {
            throw new BusinessLogicException(ResponseCode.TOKEN_IS_BLACKLIST);
        }

        if(!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new BusinessLogicException(ResponseCode.INVALID_TOKEN);
        }

        String email = jwtTokenProvider.getEmailFromRefreshToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ResponseCode.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."));

        String newAccessToken = jwtTokenProvider.generateAccessToken(user);

        return ReissueResponseDto.of(newAccessToken);
    }
}
