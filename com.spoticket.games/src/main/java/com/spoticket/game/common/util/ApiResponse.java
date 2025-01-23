package com.spoticket.game.common.util;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import com.spoticket.game.common.exception.CustomException;

public record ApiResponse<T>(Integer code, String msg, T data) {

  public static <T> ApiResponse<T> ok(T data) {
    return new ApiResponse<>(OK.value(), OK.getReasonPhrase(), data);
  }

  public static <T> ApiResponse<T> created(T data) {
    return new ApiResponse<>(CREATED.value(), CREATED.getReasonPhrase(), data);
  }

  public static <T> ApiResponse<T> noContent() {
    return new ApiResponse<>(NO_CONTENT.value(), NO_CONTENT.getReasonPhrase(), null);
  }

  public static <T> ApiResponse<T> failed(CustomException e) {
    return new ApiResponse<>(e.getCode(), e.getMsg(), null);
  }

}
