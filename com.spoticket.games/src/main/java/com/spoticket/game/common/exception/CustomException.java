package com.spoticket.game.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {

  private final Integer code;
  private final String msg;

  public CustomException(HttpStatus status) {
    code = status.value();
    msg = status.getReasonPhrase();
  }

}
