package com.spoticket.payment.presentation.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ApiSuccessResponse<T> {

    private int code;
    private String msg;
    private T data;


    public static <T> ApiSuccessResponse<T> success(T data) {
        return ApiSuccessResponse.<T>builder()
            .code(200)
            .msg("success")
            .data(data)
            .build();

    }


}
