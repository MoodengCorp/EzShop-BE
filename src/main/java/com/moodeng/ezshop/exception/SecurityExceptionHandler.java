package com.moodeng.ezshop.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodeng.ezshop.dto.response.CommonResponse;
import com.moodeng.ezshop.dto.response.ResponseCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 401 Error
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        writeErrorResponse(response, ResponseCode.UNAUTHORIZED);
    }

    // 403 Error
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        writeErrorResponse(response, ResponseCode.FORBIDDEN);
    }

    private void writeErrorResponse(HttpServletResponse response, ResponseCode responseCode) throws IOException {

        CommonResponse<Void> commonResponse = CommonResponse.ofFailure(responseCode);

        response.setStatus(responseCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        response.getWriter().write(objectMapper.writeValueAsString(commonResponse));
    }
}
