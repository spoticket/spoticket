package com.spoticket.teamstadium.exception;

public class BusinessException extends RuntimeException {

  private final int code;

  public BusinessException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.code = errorCode.getCode();
  }

  public int getCode() {
    return code;
  }
}
