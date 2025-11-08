package com.moodeng.ezshop.service;

import com.moodeng.ezshop.dto.request.LoginRequestDto;
import com.moodeng.ezshop.dto.request.SignupRequestDto;
import com.moodeng.ezshop.dto.response.LoginResponseDto;
import com.moodeng.ezshop.entity.User;
import com.moodeng.ezshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequestDto signupDto) {
        // Email Duplicate check
        if (userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        String encodedPassword = passwordEncoder.encode(signupDto.getPassword());

        User user = signupDto.toEntity(encodedPassword);

        userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequestDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return LoginResponseDto.fromEntity(user);
    }
}
