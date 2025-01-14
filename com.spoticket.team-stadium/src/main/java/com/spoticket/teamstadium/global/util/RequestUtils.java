package com.spoticket.teamstadium.global.util;

import com.spoticket.teamstadium.domain.model.UserRoleEnum;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestUtils {

  private RequestUtils() {
    throw new UnsupportedOperationException("유틸리티 클래스");
  }

  public static UUID getCurrentUserId() {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    if (attributes == null) {
      return null;
    }

    HttpServletRequest request = attributes.getRequest();
    return UUID.fromString(request.getHeader("X-User-Id"));
  }

  public static UserRoleEnum getCurrentUserRole() {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    if (attributes == null) {
      return null;
    }

    HttpServletRequest request = attributes.getRequest();
    return UserRoleEnum.valueOf(request.getHeader("X-Role"));
  }
}