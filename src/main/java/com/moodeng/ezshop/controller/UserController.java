package com.moodeng.ezshop.controller;

import com.moodeng.ezshop.auth.JwtTokenProvider;
import com.moodeng.ezshop.dto.request.LoginRequestDto;
import com.moodeng.ezshop.dto.request.SignupRequestDto;
import com.moodeng.ezshop.dto.response.CommonResponse;
import com.moodeng.ezshop.dto.response.LoginResponseDto;
import com.moodeng.ezshop.dto.response.ProfileResponseDto;
import com.moodeng.ezshop.dto.service.LoginDetails;
import com.moodeng.ezshop.service.UserService;
import com.moodeng.ezshop.util.CookieUtils;
import com.moodeng.ezshop.util.RequestUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<Void>> signup(@RequestBody SignupRequestDto signupRequestDto) {

        userService.signup(signupRequestDto);

        return ResponseEntity.ok(CommonResponse.ofSuccess());
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {

        LoginDetails loginDetails = userService.login(loginRequestDto);

        int maxAgeSeconds = (int) (jwtTokenProvider.getRefreshExpirationTime() / 1000);
        CookieUtils.addRefreshTokenCookie(response, loginDetails.getRefreshToken(), maxAgeSeconds);

        return ResponseEntity.ok(CommonResponse.ofSuccess(loginDetails.getLoginResponseDto()));
    }

    @GetMapping("/logout")
    public ResponseEntity<CommonResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response, @CookieValue(value = CookieUtils.REFRESH_TOKEN_COOKIE, required = false) Cookie refreshTokenCookie) {
        String accessToken = RequestUtils.extractToken(request);
        String refreshToken = CookieUtils.getRefreshToken(refreshTokenCookie);

        userService.logout(accessToken, refreshToken);

        CookieUtils.expireRefreshTokenCookie(response);

        return ResponseEntity.ok(CommonResponse.ofSuccess());
    }

    @GetMapping("/info")
    public ResponseEntity<CommonResponse<ProfileResponseDto>> getProfileInfo(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();

        ProfileResponseDto profileResponseDto = userService.getProfileInfo(email);

        return ResponseEntity.ok(CommonResponse.ofSuccess(profileResponseDto));
    }
}
