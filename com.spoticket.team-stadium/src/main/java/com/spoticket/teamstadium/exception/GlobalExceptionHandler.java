package com.spoticket.teamstadium.exception;

import com.spoticket.teamstadium.global.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  // 비즈니스 로직 관련 예외 처리
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
    ErrorResponse errorResponse = new ErrorResponse(ex.getCode(), ex.getMessage());
    return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getCode()));
  }

  // 요청값 유효성 검사 실패
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex) {
    ErrorResponse errorResponse = new ErrorResponse(400, "요청값이 유효하지 않습니다");
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  // JSON 데이터를 직렬화/역직렬화시 문제 발생
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException ex) {
    ErrorResponse errorResponse = new ErrorResponse(400, "요청값이 유효하지 않습니다");
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  // 서버에서 발생하는 기타 예외 처리
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    ErrorResponse errorResponse = new ErrorResponse(500, "서버 에러가 발생했습니다");
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
