package com.moodeng.ezshop.exception;

import com.moodeng.ezshop.dto.response.CommonResponse;
import com.moodeng.ezshop.dto.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<CommonResponse<Void>> handleBusinessLogicException(BusinessLogicException e) {
        log.warn("BusinessLogicException [{}]: {}", e.getResponseCode(), e.getMessage());

        return ResponseEntity
                .status(e.getResponseCode().getHttpStatus())
                .body(CommonResponse.ofFailure(e.getResponseCode(), e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("IllegalArgumentException: {}", e.getMessage());

        return ResponseEntity
                .status(ResponseCode.BAD_REQUEST.getHttpStatus())
                .body(CommonResponse.ofFailure(ResponseCode.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleException(Exception e) {
        log.warn("Exception : ", e);

        return ResponseEntity
                .status(ResponseCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(CommonResponse.ofFailure(ResponseCode.INTERNAL_SERVER_ERROR));
    }
}
