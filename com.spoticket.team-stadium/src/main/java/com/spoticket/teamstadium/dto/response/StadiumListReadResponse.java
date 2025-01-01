package com.spoticket.teamstadium.dto.response;

import com.spoticket.teamstadium.domain.Stadium;
import java.util.UUID;

public record StadiumListReadResponse(
    UUID stadiumId,
    String name,
    String address,
    String seatImage,
    String description
) {

  public static StadiumListReadResponse from(Stadium stadium) {
    return new StadiumListReadResponse(
        stadium.getStadiumId(),
        stadium.getName(),
        stadium.getAddress(),
        stadium.getSeatImage(),
        stadium.getDescription()
    );
  }
}
