package com.spoticket.user.global.util;

import com.spoticket.user.global.exception.CustomException;
import lombok.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtils {

    public static <T> DataResponse<T> ok(T data) {
        return DataResponse.<T>builder()
                .code(OK.value())
                .msg(OK.getReasonPhrase())
                .data(data)
                .build();
    }

    public static <T> DataResponse<T> created(T data) {
        return DataResponse.<T>builder()
                .code(CREATED.value())
                .msg(CREATED.getReasonPhrase())
                .data(data)
                .build();
    }

    public static BasicResponse noContent() {
        return BasicResponse.builder()
                .code(NO_CONTENT.value())
                .msg(NO_CONTENT.getReasonPhrase())
                .build();
    }

    public static BasicResponse failed(CustomException e) {
        return BasicResponse.builder()
                .code(e.getCode())
                .msg(e.getMsg())
                .build();
    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class BasicResponse {

        private Integer code;
        private String msg;
    }

    @AllArgsConstructor
    @Getter
    @Builder
    public static class DataResponse<T> {

        private Integer code;
        private String msg;
        private T data;
    }

}