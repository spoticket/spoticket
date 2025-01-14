package com.spoticket.user.global.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseStatus {

    USER_SIGN_UP(HttpStatus.CREATED, "회원가입에 성공했습니다."),
    USER_LOGIN(HttpStatus.OK, "로그인 성공"),

    USER_ROLE_CHANGED(HttpStatus.NO_CONTENT, "회원 권한이 변경되었습니다."),
    USER_INFO_UPDATE(HttpStatus.NO_CONTENT, "회원정보가 수정되었습니다."),
    USER_DELETE(HttpStatus.NO_CONTENT, "회원탈퇴 되었습니다."),

    COUPON_CREATE(HttpStatus.CREATED, "쿠폰이 생성되었습니다."),
    COUPON_UPDATE(HttpStatus.NO_CONTENT, "쿠폰이 생성되었습니다."),
    COUPON_DELETE(HttpStatus.NO_CONTENT, "쿠폰이 삭제되었습니다."),

    COUPON_ISSUE(HttpStatus.CREATED, "쿠폰이 발급되었습니다."),
    COUPON_USED(HttpStatus.NO_CONTENT, "쿠폰이 사용되었습니다."),
    ;
    private final HttpStatus status;
    private final String message;
}

