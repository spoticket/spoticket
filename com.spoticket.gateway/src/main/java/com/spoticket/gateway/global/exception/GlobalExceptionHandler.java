package com.spoticket.gateway.global.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.spoticket.gateway.global.exception.ErrorResponse.ValidationError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.spoticket.gateway.global.exception.ErrorStatus.INTERNAL_SERVER_ERROR;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * GlobalException(커스텀 에러) 처리
     *
     * @param e GlobalException
     * @return ResponseEntity
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        ErrorStatus errorCode = e.getErrorCode();
        log.error("GlobalException occurred : ErrorCode = {} message = {}",
                  errorCode.name(), errorCode.getMessage());
        return handleExceptionInternal(errorCode);
    }

    /**
     * MethodArgumentNotValidException 처리 (Validation 실패 등)
     *
     * @param e MethodArgumentNotValidException
     * @return ResponseEntity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        // 모든 유효성 검증 오류를 가져와서 메시지를 구성
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * IllegalArgumentException 처리 (적절하지 않은 파라미터)
     *
     * @param e IllegalArgumentException
     * @return ResponseEntity
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException e) {
        log.error("IllegalArgumentException occurred", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    /**
     * Exception 처리
     *
     * @param e Exception
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        log.error("Unexpected Exception occurred", e);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(INTERNAL_SERVER_ERROR.getMessage());
    }

    private ResponseEntity<Object> handleExceptionInternal(ErrorStatus errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponseBody(errorCode));
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorStatus errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponseBody((MethodArgumentNotValidException) e, errorCode));
    }

    private ErrorResponse makeErrorResponseBody(ErrorStatus errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .build();
    }

    private ErrorResponse makeErrorResponseBody(BindException e, ErrorStatus errorCode) {
        List<ValidationError> validationErrorList = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(ValidationError::of)
                .collect(Collectors.toList());

        return ErrorResponse.builder()
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .errors(validationErrorList)
                .build();
    }

}