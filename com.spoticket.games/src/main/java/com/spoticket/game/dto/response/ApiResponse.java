package com.spoticket.game.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

  private final int code;
  private final String msg;
  private final T data;

  public static <T> ApiResponse<T> success(T data, String msg) {
    return new ApiResponse<>(200, msg, data);
  }

  public static <T> ApiResponse<T> fail(int code, String msg) {
    return new ApiResponse<>(code, msg, null);
  }
}
