package com.spoticket.ticket.global.util;

import lombok.Getter;

@Getter
public class PagedApiResponse<T> extends ApiResponse<T> {

  private final int page;
  private final int size;
  private final int totalCount;

  public PagedApiResponse(int code, String msg, T data, int page, int size, int totalCount) {
    super(code, msg, data);
    this.page = page;
    this.size = size;
    this.totalCount = totalCount;
  }

  public static <T> PagedApiResponse<T> success(T data, String msg, int page, int size,
      int totalCount) {
    return new PagedApiResponse<>(200, msg, data, page, size, totalCount);
  }

}
