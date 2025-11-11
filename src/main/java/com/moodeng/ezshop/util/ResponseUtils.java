package com.moodeng.ezshop.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moodeng.ezshop.dto.response.CommonResponse;
import com.moodeng.ezshop.dto.response.ResponseCode;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public final class ResponseUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ResponseUtils() {
    }

    public static void writeErrorResponse(HttpServletResponse response, ResponseCode code) throws IOException {

        CommonResponse<Void> commonResponse = CommonResponse.ofFailure(code);

        response.setStatus(code.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(commonResponse));
    }
}