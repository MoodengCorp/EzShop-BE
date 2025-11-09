package com.moodeng.ezshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CSRF 비활성화 (JWT 사용 API 서버)
                .csrf(AbstractHttpConfigurer::disable)

                // 2. 세션 정책을 STATELESS로 설정 (JWT 사용)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3. API 경로별 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // [중요!] /user/ (회원가입, 로그인) 경로는 누구나 접근 허용
                        .requestMatchers("/user/**").permitAll()

                        // [중요!] /item/ (상품) 경로도 테스트를 위해 일단 누구나 접근 허용
                        .requestMatchers("/item/**").permitAll()

                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
