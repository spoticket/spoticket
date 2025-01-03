package com.spoticket.teamstadium.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

  DUPLICATE_TEAM_NAME(400, "중복된 팀명입니다"),
  DUPLICATE_STADIUM_NAME(400, "중복된 경기장 이름입니다"),
  TEAM_NOT_FOUND(404, "해당하는 팀이 없습니다"),
  NOT_FOUND(404, "요청한 리소스를 찾을 수 없습니다"),
  GAME_DATA_FETCH_FAILED(500, "게임 데이터를 가져오지 못했습니다."),
  SERVER_ERROR(500, "서버 에러가 발생했습니다");

  private final int code;
  private final String message;

  ErrorCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

}
