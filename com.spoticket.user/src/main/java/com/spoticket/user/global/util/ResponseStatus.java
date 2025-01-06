package com.spoticket.user.global.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseStatus {

    USER_SIGN_UP(HttpStatus.CREATED, "회원가입에 성공했습니다."),
    USER_LOGIN(HttpStatus.OK, "로그인 성공"),

    USER_ROLE_CHANGED(HttpStatus.NO_CONTENT, "회원 권한 변경이 성공하였습니다.")
    ;
    private final HttpStatus status;
    private final String message;
}

