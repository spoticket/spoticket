package com.spoticket.teamstadium.dto.response;

import com.spoticket.teamstadium.domain.Stadium;
import java.util.UUID;

public record StadiumInfoResponse(
    UUID stadiumId,
    String name,
    String address,
    String seatImage,
    String description
) {

  public static StadiumInfoResponse from(Stadium stadium) {
    return new StadiumInfoResponse(
        stadium.getStadiumId(),
        stadium.getName(),
        stadium.getAddress(),
        stadium.getSeatImage(),
        stadium.getDescription()
    );
  }
}
