package com.spoticket.teamstadium.application.dto.response;

import com.spoticket.teamstadium.domain.model.Seat;

public record SeatReadResponse(
    String section,
    Long quantity,
    Integer price
) {

  public static SeatReadResponse from(Seat seat) {
    return new SeatReadResponse(
        seat.getSection(),
        seat.getQuantity(),
        seat.getPrice()
    );
  }
}
