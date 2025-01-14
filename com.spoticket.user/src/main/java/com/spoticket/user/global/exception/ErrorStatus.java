package com.spoticket.user.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus {

    // global
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "페이지를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    NOT_AUTHENTICATED_USER(HttpStatus.UNAUTHORIZED, "인증된 사용자가 아닙니다."),
    UNEXPECTED_PRINCIPAL_TYPE(HttpStatus.BAD_REQUEST, "예상치 못한 Principal 타입입니다."),

    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "아이디나 비밀번호가 맞지 않습니다."),
    USER_DUPLICATE(HttpStatus.CONFLICT, "이미 등록된 이메일입니다."),

    // coupon
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "쿠폰을 찾을 수 없습니다."),
    COUPON_DUPLICATE(HttpStatus.CONFLICT, "중복된 쿠폰명 입니다."),
    COUPON_ENABLE(HttpStatus.CONFLICT, "발급이 불가능한 쿠폰입니다."),
    COUPON_MATCH(HttpStatus.CONFLICT, "올바른 소유주가 아닙니다."),
    COUPON_DEACTIVATE(HttpStatus.CONFLICT, "사용 가능한 쿠폰이 아닙니다."),

    ;
    private final HttpStatus httpStatus;
    private final String message;
}
