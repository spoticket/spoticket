package com.spoticket.game.global.exception;

import static com.spoticket.game.global.util.ResponseUtils.failed;

import com.spoticket.game.global.util.ResponseUtils.DataResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public DataResponse<Object> handleCustomException(CustomException e) {
    return failed(e);
  }

}
