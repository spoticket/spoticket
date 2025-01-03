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

    private int status;
    private String message;
    private T data;


    public static <T> ApiSuccessResponse<T> success(T data) {
        return ApiSuccessResponse.<T>builder()
            .status(200)
            .message("success")
            .data(data)
            .build();

    }


}
