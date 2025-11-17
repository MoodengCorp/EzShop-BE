package com.moodeng.ezshop.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {

    private final boolean success;
    private final T data;
    private final ErrorResponse error;

    public static <T> CommonResponse<T> ofSuccess(T data) {
        return new CommonResponse<>(true, data, null);
    }

    public static CommonResponse<Void> ofSuccess() {
        return new CommonResponse<>(true, null, null);
    }

    public static CommonResponse<Void> ofFailure(ResponseCode code) {
        return new CommonResponse<>(false, null, ErrorResponse.of(code));
    }

    public static CommonResponse<Void> ofFailure(ResponseCode code, String customMessage) {
        return new CommonResponse<>(false, null, ErrorResponse.of(code, customMessage));
    }
}

