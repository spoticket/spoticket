package com.spoticket.game.global.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.spoticket.game.global.util.ResponseUtils.BasicResponse;
import static com.spoticket.game.global.util.ResponseUtils.failed;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public BasicResponse handleCustomException(CustomException e) {
        return failed(e);
    }

}
