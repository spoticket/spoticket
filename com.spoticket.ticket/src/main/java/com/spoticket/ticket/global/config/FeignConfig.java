package com.spoticket.ticket.global.config;

import com.spoticket.ticket.global.exception.CustomErrorDecoder;
import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

  // Feign 로깅 설정
  @Bean
  public Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL; // 요청/응답의 상세 로그를 출력
  }

  // ErrorDecoder 설정
  @Bean
  public ErrorDecoder errorDecoder() {
    return new CustomErrorDecoder();
  }

}
