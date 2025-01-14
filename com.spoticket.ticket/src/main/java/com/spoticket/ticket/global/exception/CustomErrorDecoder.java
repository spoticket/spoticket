package com.spoticket.ticket.global.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomErrorDecoder implements ErrorDecoder {

  private static final Logger logger = LoggerFactory.getLogger(CustomErrorDecoder.class);

  @Override
  public Exception decode(String methodKey, Response response) {

    // 로그 출력
    logger.error("Feign 호출 오류: methodKey={}, status={}, reason={}",
        methodKey, response.status(), response.reason());

    HttpStatus status = HttpStatus.valueOf(response.status());

    switch (status) {
      case NOT_FOUND: // 404
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없습니다.");
      case BAD_REQUEST: // 400
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
      case INTERNAL_SERVER_ERROR: // 500
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류 발생");
      default:
        return new Exception("Feign 호출 중 알 수 없는 오류가 발생했습니다.");
    }

  }
}
