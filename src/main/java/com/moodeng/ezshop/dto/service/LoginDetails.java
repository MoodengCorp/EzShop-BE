package com.moodeng.ezshop.dto.service;

import com.moodeng.ezshop.dto.response.LoginResponseDto;
import lombok.Getter;

@Getter
public class LoginDetails {

    private final LoginResponseDto loginResponseDto;
    private final String refreshToken;

    public LoginDetails(LoginResponseDto loginResponseDto, String refreshToken) {
        this.loginResponseDto = loginResponseDto;
        this.refreshToken = refreshToken;
    }
}
