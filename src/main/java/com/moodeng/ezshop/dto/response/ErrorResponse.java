package com.moodeng.ezshop.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private final String type;
    private final String message;

    public static ErrorResponse of(ResponseCode code) {
        return new ErrorResponse(code.name(), code.getMessage());
    }

    public static ErrorResponse of(ResponseCode code, String customMessage) {
        return new ErrorResponse(code.name(), customMessage);
    }
}