package com.moodeng.ezshop.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

    // Common
    SUCCESS(HttpStatus.OK, "요청에 성공했습니다."),
    CREATED(HttpStatus.CREATED, "리소스를 성공적으로 생성했습니다."),

    // User
    SIGNUP_SUCCESS(HttpStatus.OK, "회원 가입이 완료되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인되었습니다."),
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 되었습니다."),
    SIGNOUT_SUCCESS(HttpStatus.OK, "회원 탈퇴가 완료되었습니다."),

    // Errors
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 일치하지 않습니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자 입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}