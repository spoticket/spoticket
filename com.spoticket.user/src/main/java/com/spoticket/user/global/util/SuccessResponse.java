package com.spoticket.user.global.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record SuccessResponse<T>(
        Integer code,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL) T data
) {

    public static <T> SuccessResponse<T> of(ResponseStatus status, T data) {
        return SuccessResponse.<T>builder()
                .code(status.getStatus().value())
                .message(status.getMessage())
                .data(data)
                .build();
    }

    public static <T> SuccessResponse<T> of(ResponseStatus status) {
        return SuccessResponse.<T>builder()
                .code(status.getStatus().value())
                .message(status.getMessage())
                .build();
    }


    public static <T> SuccessResponse<T> ok(T data) {
        return SuccessResponse.<T>builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> SuccessResponse<T> noContent() {
        return SuccessResponse.<T>builder()
                .code(HttpStatus.OK.value())
                .message("Success")
                .build();
    }
}
