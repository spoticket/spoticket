package com.spoticket.payment.domain.payment.exception;


import com.spoticket.payment.domain.common.BaseException;
import lombok.Getter;

@Getter
public class PaymentException extends BaseException {

    public PaymentException(PaymentErrorCode errorCode) {
        super(errorCode);
    }

}

