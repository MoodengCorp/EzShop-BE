package com.moodeng.ezshop.auth;

import com.moodeng.ezshop.dto.response.ResponseCode;
import com.moodeng.ezshop.exception.BusinessLogicException;
import com.moodeng.ezshop.util.RequestUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = RequestUtils.extractToken(request);

            if (StringUtils.hasText(token)) {
                checkBlacklistedToken(token);

                authenticateWithToken(token);
            }
        } catch (BusinessLogicException e) {
            request.setAttribute("jwtException", e.getResponseCode());
            log.warn("SecurityException: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private void checkBlacklistedToken(String token) {
        if (redisTemplate.hasKey(token)) {
            throw new BusinessLogicException(ResponseCode.TOKEN_IS_BLACKLIST);
        }
    }
    private void authenticateWithToken(String token) {
        jwtTokenProvider.validateAccessToken(token);

        String email = jwtTokenProvider.getEmailFromAccessToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
