package com.spoticket.ticket.global.util;

import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class UserContextUtil {

  // HttpServletRequest를 가져오기 위해 RequestContextHolder 사용
  public static HttpServletRequest getRequest() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes != null) {
      return attributes.getRequest();
    }
    return null;  // null일 경우, 요청이 없거나 서블릿 환경이 아닌 경우
  }

  // X-User-Id 헤더 값 추출
  public UUID getUserId() {
    HttpServletRequest request = getRequest();
    if (request != null) {
      return UUID.fromString(request.getHeader("X-User-Id"));
    }
    return null;
  }

  // X-Role 헤더 값 추출
  public String getUserRole() {
    HttpServletRequest request = getRequest();
    if (request != null) {
      return request.getHeader("X-Role");
    }
    return null;
  }
}
