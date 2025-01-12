package com.spoticket.payment.domain.payment.exception;

import com.spoticket.payment.domain.common.ErrorCode;
import lombok.Getter;

@Getter
public enum PaymentErrorCode implements ErrorCode {
    PAYMENT_NOT_FOUND(404, "결제 정보를 찾을 수 없습니다"),
    AMOUNT_NOT_EQUAL(400, "결제 금액이 일치하지 않습니다."),
    PAYMENT_ALREADY_PROCESSED(400, "이미 처리된 결제입니다, 다시 시도 해주세요" );

    private final int code;
    private final String message;

    PaymentErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
