package com.spoticket.teamstadium.domain.model;

import lombok.Getter;

@Getter
public enum TeamCategoryEnum {

  HANDBALL("핸드볼"),
  ICE_HOCKEY("아이스하키"),
  SOCCER("축구"),
  BASEBALL("야구"),
  BASKETBALL("농구"),
  VOLLEYBALL("배구"),
  OTHER("기타"),
  E_SPORTS("e스포츠");

  private final String koreanName;

  TeamCategoryEnum(String koreanName) {
    this.koreanName = koreanName;
  }

}
