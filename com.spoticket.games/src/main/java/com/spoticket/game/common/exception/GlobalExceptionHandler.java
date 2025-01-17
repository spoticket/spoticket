package com.spoticket.game.common.exception;


import static com.spoticket.game.common.util.ApiResponse.failed;

import com.spoticket.game.common.util.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ApiResponse<Object> handleCustomException(CustomException e) {
    return failed(e);
  }

}
