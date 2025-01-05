package com.spoticket.ticket.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

  BAD_REQUEST_EXCEPTION(400, "잘못된 요청입니다."),

  // 티켓 관련 오류
  SEAT_ALREADY_RESERVED(400, "이미 예약된 좌석입니다."),
  TICKET_NOT_FOUND(404, "티켓을 찾을 수 없습니다."),

  // 인증 오류
  UNAUTHORIZED_EXCEPTION(401, "인증에 실패하였습니다."),

  // 그 외 서버오류
  INTERNAL_SERVER_ERROR(500, "서버에서 예기치 않은 오류가 발생했습니다.");

  private final int status;
  private final String message;

  ErrorCode(int status, String message) {
    this.status = status;
    this.message = message;
  }
}
