package com.spoticket.game.global.util;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.spoticket.game.global.exception.CustomException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

  public static <T> DataResponse<T> noContent() {
    return DataResponse.<T>builder()
        .code(NO_CONTENT.value())
        .msg(NO_CONTENT.getReasonPhrase())
        .data(null)
        .build();
  }

  public static <T> DataResponse<T> failed(CustomException e) {
    return DataResponse.<T>builder()
        .code(e.getCode())
        .msg(e.getMsg())
        .data(null)
        .build();
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
