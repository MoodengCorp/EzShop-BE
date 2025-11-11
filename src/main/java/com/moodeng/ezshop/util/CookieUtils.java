package com.moodeng.ezshop.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;

public class CookieUtils {

    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    public static String getRefreshToken(Cookie refreshTokenCookie) {
        if (refreshTokenCookie != null && StringUtils.hasText(refreshTokenCookie.getValue())) {
            return refreshTokenCookie.getValue();
        }
        return null;
    }

    public static void addRefreshTokenCookie(HttpServletResponse response, String token, long maxAgeSeconds) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        //cookie.setSecure(true);
        cookie.setMaxAge((int) maxAgeSeconds);

        response.addCookie(cookie);
    }

    public static void expireRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        //cookie.setSecure(true);
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }
}
