package com.spoticket.teamstadium.exception;

public class NotFoundException extends RuntimeException {

  private final int code;

  public NotFoundException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.code = errorCode.getCode();
  }

  public int getCode() {
    return code;
  }
}
