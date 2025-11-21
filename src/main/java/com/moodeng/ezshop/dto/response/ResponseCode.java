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
    REISSUE_SUCCESS(HttpStatus.OK, "토큰 재발급에 성공했습니다."),


    // Item
    ITEM_CREATED_SUCCESS(HttpStatus.CREATED,"상품을 성공적으로 등록했습니다."),

    // Errors
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 일치하지 않습니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자 입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    TOKEN_IS_BLACKLIST(HttpStatus.UNAUTHORIZED, "로그아웃된 사용자 입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효한 토큰이 아닙니다."),

    NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),

    // Item_Error
    SELLER_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않은 판매자 정보입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "등록되지 않은 사용자 정보입니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않은 카테고리 정보입니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    INVALID_FILTER_FORMAT(HttpStatus.BAD_REQUEST, "상품 목록조회 필터 형식이 올바르지 않습니다. (예: price:5000-10000)"),
    INVALID_ITEM_STATUS_UPDATE(HttpStatus.BAD_REQUEST, "변경할 수 없는 상품 상태입니다."),

    // Order_Error
    INVALID_CART_ITEM(HttpStatus.BAD_REQUEST, "유효하지 않은 장바구니 상품이 포함되어 있습니다."),
    ITEM_NOT_FOR_SALE(HttpStatus.BAD_REQUEST, "현재 구매할 수 없는 상품입니다."),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "상품의 재고가 부족합니다.."),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}