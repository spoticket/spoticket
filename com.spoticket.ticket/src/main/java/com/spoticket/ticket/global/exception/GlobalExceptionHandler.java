package com.spoticket.ticket.global.exception;

import com.spoticket.ticket.global.util.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException ex) {
    ErrorCode errorCode = ex.getErrorCode();
    return ResponseEntity
        .status(errorCode.getStatus())
        .body(ApiResponse.fail(errorCode.getStatus(), errorCode.getMessage()));
  }

  // 서버 오류
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<?>> handleGeneralException(Exception ex) {
    return ResponseEntity
        .status(500)
        .body(ApiResponse.fail(500, "서버에서 예기치 않은 오류가 발생했습니다."));
  }
}
