package com.moodeng.ezshop.controller;

import com.moodeng.ezshop.auth.JwtTokenProvider;
import com.moodeng.ezshop.dto.request.LoginRequestDto;
import com.moodeng.ezshop.dto.request.PasswordCheckRequestDto;
import com.moodeng.ezshop.dto.request.ProfileUpdateRequestDto;
import com.moodeng.ezshop.dto.request.SignupRequestDto;
import com.moodeng.ezshop.dto.response.*;
import com.moodeng.ezshop.dto.service.LoginDetails;
import com.moodeng.ezshop.exception.BusinessLogicException;
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

    @PatchMapping()
    public ResponseEntity<CommonResponse<Void>> updateProfileInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ProfileUpdateRequestDto updateDto
    ) {
        String email = userDetails.getUsername();

        userService.updateProfileInfo(email, updateDto);

        return ResponseEntity.ok(CommonResponse.ofSuccess());
    }

    @DeleteMapping()
    public ResponseEntity<CommonResponse<Void>> signout(
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest request,
            HttpServletResponse response,
            @CookieValue(value = CookieUtils.REFRESH_TOKEN_COOKIE, required = false) Cookie refreshTokenCookie
    ) {
        String email = userDetails.getUsername();

        userService.signout(email);

        String accessToken = RequestUtils.extractToken(request);
        String refreshToken = CookieUtils.getRefreshToken(refreshTokenCookie);

        userService.logout(accessToken, refreshToken);

        CookieUtils.expireRefreshTokenCookie(response);

        return ResponseEntity.ok(CommonResponse.ofSuccess());
    }

    @PostMapping("/reissue")
    public ResponseEntity<CommonResponse<ReissueResponseDto>> reissue(
            @CookieValue(value = CookieUtils.REFRESH_TOKEN_COOKIE, required = false) String refreshToken
    ) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new BusinessLogicException(ResponseCode.INVALID_TOKEN, "리프레시 토큰이 없습니다.");
        }

        ReissueResponseDto reissueResponseDto = userService.reissue(refreshToken);

        return ResponseEntity.ok(CommonResponse.ofSuccess(reissueResponseDto));
    }

    @PostMapping("/passwordcheck")
    public ResponseEntity<CommonResponse<Void>> checkPassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PasswordCheckRequestDto passwordDto
    ) {
        String email = userDetails.getUsername();

        userService.checkPassword(email, passwordDto.getPassword());

        return ResponseEntity.ok(CommonResponse.ofSuccess());
    }
}
