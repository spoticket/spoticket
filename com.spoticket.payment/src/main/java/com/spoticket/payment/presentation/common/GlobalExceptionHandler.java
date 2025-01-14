package com.spoticket.payment.presentation.common;

import com.spoticket.payment.domain.common.BaseException;
import com.spoticket.payment.domain.payment.exception.PaymentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiErrorResponse> handlePaymentException(BaseException e) {
        return ResponseEntity.badRequest()
            .body(ApiErrorResponse.of(e.getErrorCode()));
    }
}