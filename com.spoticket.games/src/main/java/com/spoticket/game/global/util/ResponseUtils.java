package com.spoticket.game.global.util;

import com.spoticket.game.global.exception.CustomException;
import lombok.*;

import static org.springframework.http.HttpStatus.OK;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtils {

    public static <T> DataResponse<T> ok(T data) {
        return DataResponse.<T>builder()
                .code(OK.value())
                .msg(OK.getReasonPhrase())
                .data(data)
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
