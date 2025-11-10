package com.moodeng.ezshop.controller;

import com.moodeng.ezshop.auth.JwtTokenProvider;
import com.moodeng.ezshop.dto.request.LoginRequestDto;
import com.moodeng.ezshop.dto.request.SignupRequestDto;
import com.moodeng.ezshop.dto.response.LoginResponseDto;
import com.moodeng.ezshop.dto.service.LoginDetails;
import com.moodeng.ezshop.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequestDto signupRequestDto) {

        userService.signup(signupRequestDto);

        return new ResponseEntity<>("회원 가입이 완료되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {

        LoginDetails loginDetails = userService.login(loginRequestDto);

        Cookie refreshTokenCookie = new Cookie("refreshToken", loginDetails.getRefreshToken());

        // Cookie setting
        refreshTokenCookie.setHttpOnly(true);
        //refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");

        int maxAge = (int) (jwtTokenProvider.getRefreshExpirationTime() / 1000);
        refreshTokenCookie.setMaxAge(maxAge);

        response.addCookie(refreshTokenCookie);
        return new ResponseEntity<>(loginDetails.getLoginResponseDto(), HttpStatus.OK);
    }
}
