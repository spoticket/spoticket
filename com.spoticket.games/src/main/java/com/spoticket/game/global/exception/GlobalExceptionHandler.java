package com.spoticket.game.global.exception;

import static com.spoticket.game.global.util.ResponseUtils.BasicResponse;
import static com.spoticket.game.global.util.ResponseUtils.failed;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public BasicResponse handleCustomException(CustomException e) {
    return failed(e);
  }

}
