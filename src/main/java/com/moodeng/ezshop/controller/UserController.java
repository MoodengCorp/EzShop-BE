package com.moodeng.ezshop.controller;

import com.moodeng.ezshop.dto.request.LoginRequestDto;
import com.moodeng.ezshop.dto.request.SignupRequestDto;
import com.moodeng.ezshop.dto.response.LoginResponseDto;
import com.moodeng.ezshop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupDto) {

        userService.signup(signupDto);

        return new ResponseEntity<>("회원 가입이 완료되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginDto) {
        LoginResponseDto loginUser = userService.login(loginDto);
        return new ResponseEntity<>(loginUser, HttpStatus.OK);
    }
}
