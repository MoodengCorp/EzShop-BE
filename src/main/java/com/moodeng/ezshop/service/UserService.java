package com.moodeng.ezshop.service;

import com.moodeng.ezshop.dto.user.SignupDto;
import com.moodeng.ezshop.entity.User;
import com.moodeng.ezshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void signup(SignupDto signupDto) {
        // Email Duplicate check
        if (userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        User user = signupDto.toEntity();

        userRepository.save(user);
    }
}
