package com.spoticket.teamstadium.application.dto.response;

import com.spoticket.teamstadium.domain.model.Stadium;
import java.util.UUID;

public record StadiumListReadResponse(
    UUID stadiumId,
    String name,
    String address
) {

  public static StadiumListReadResponse from(Stadium stadium) {
    return new StadiumListReadResponse(
        stadium.getStadiumId(),
        stadium.getName(),
        stadium.getAddress()
    );
  }
}
