package com.moodeng.ezshop.auth;


import com.moodeng.ezshop.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

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

    private Claims getClaimsFromToken(String token, SecretKey key, boolean allowExpired) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            if (allowExpired) {
                return e.getClaims();
            }
            throw e;
        }
    }

    public String getEmailFromAccessToken(String token) {
        return getClaimsFromToken(token, accessKey, true).getSubject();
    }

    public String getEmailFromRefreshToken(String token) {
        return getClaimsFromToken(token, refreshKey, true).getSubject();
    }

    public Long getRemainingExpirationTimeFromAccessToken(String token) {
        return calculateRemainingTime(token, accessKey);
    }

    public Long getRemainingExpirationTimeFromRefreshToken(String token) {
        return calculateRemainingTime(token, refreshKey);
    }

    private Long calculateRemainingTime(String token, SecretKey key) {
        try {
            Date expiration = getClaimsFromToken(token, key, true).getExpiration();
            Date now = new Date();

            long remainingTime = expiration.getTime() - now.getTime();

            return (remainingTime > 0) ? remainingTime : 0;
        } catch (Exception e) {
            return 0L;
        }
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().verifyWith(accessKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser().verifyWith(refreshKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
