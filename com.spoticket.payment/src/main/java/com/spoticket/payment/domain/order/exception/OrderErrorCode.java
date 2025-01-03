package com.spoticket.payment.domain.order.exception;


import com.spoticket.payment.domain.common.ErrorCode;
import lombok.Getter;

@Getter
public enum OrderErrorCode implements ErrorCode {
    ORDER_NOT_FOUND(404, "결제 정보를 찾을 수 없습니다");

    private final int code;
    private final String message;

    OrderErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
