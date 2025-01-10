package com.spoticket.teamstadium.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

  @Override
  public Exception decode(String methodKey, Response response) {
    log.error("Feign error: methodKey={}, status={}, reason={}",
        methodKey, response.status(), response.reason());
    return new RuntimeException("Feign Client Error: " + response.reason());
  }
}