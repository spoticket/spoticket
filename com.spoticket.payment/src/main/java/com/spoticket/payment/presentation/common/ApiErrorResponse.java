package com.spoticket.payment.presentation.common;

import com.spoticket.payment.domain.common.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter

public record ApiErrorResponse(int errorCode, String message) {

    public static ApiErrorResponse of(ErrorCode errorCode) {
        return new ApiErrorResponse(
            errorCode.getCode(),
            errorCode.getMessage()
        );
    }
}