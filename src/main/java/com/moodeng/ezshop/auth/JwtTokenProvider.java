package com.moodeng.ezshop.auth;


import com.moodeng.ezshop.dto.response.ResponseCode;
import com.moodeng.ezshop.entity.User;
import com.moodeng.ezshop.exception.BusinessLogicException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.access.secret}")
    private String accessSecret;
    @Value("${jwt.refresh.secret}")
    private String refreshSecret;
    @Getter
    @Value("${jwt.access.expiration-ms}")
    private Long accessExpirationTime;
    @Getter
    @Value("${jwt.refresh.expiration-ms}")
    private Long refreshExpirationTime;

    private SecretKey accessKey;
    private SecretKey refreshKey;

    @PostConstruct
    protected void init() {
        byte[] accessKeyBytes = Base64.getDecoder().decode(accessSecret);
        this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);

        byte[] refreshKeyBytes = Base64.getDecoder().decode(refreshSecret);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
    }

    public String generateAccessToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessExpirationTime);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().name())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(accessKey)
                .compact();
    }

    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpirationTime);

        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(refreshKey)
                .compact();
    }

    private Claims getClaimsFromToken(String token, SecretKey key) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getEmailFromAccessToken(String token) {
        return getClaimsFromToken(token, accessKey).getSubject();
    }

    public String getEmailFromRefreshToken(String token) {
        return getClaimsFromToken(token, refreshKey).getSubject();
    }

    public Long getRemainingExpirationTimeFromAccessToken(String token) {
        return calculateRemainingTime(token, accessKey);
    }

    public Long getRemainingExpirationTimeFromRefreshToken(String token) {
        return calculateRemainingTime(token, refreshKey);
    }

    private Long calculateRemainingTime(String token, SecretKey key) {
        try {
            Date expiration = getClaimsFromToken(token, key).getExpiration();
            Date now = new Date();

            long remainingTime = expiration.getTime() - now.getTime();

            return (remainingTime > 0) ? remainingTime : 0;
        } catch (Exception e) {
            return 0L;
        }
    }

    public void validateAccessToken(String token) {
        validateToken(token, accessKey);
    }

    public void validateRefreshToken(String token) {
        validateToken(token, refreshKey);
    }

    private void validateToken(String token, SecretKey secretKey) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            log.warn(ResponseCode.TOKEN_EXPIRED.getMessage());
            throw new BusinessLogicException(ResponseCode.TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn(ResponseCode.INVALID_TOKEN.getMessage());
            throw new BusinessLogicException(ResponseCode.INVALID_TOKEN);
        }
    }
}
