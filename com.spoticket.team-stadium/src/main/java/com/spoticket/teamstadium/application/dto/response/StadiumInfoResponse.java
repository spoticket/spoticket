package com.spoticket.teamstadium.application.dto.response;

import com.spoticket.teamstadium.domain.model.Stadium;
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
