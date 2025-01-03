package com.spoticket.user.global.exception;


import com.spoticket.user.global.util.ResponseUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.spoticket.user.global.util.ResponseUtils.failed;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseUtils.BasicResponse handleCustomException(CustomException e) {
        return failed(e);
    }

}