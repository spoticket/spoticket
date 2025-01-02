package com.spoticket.teamstadium.dto.response;

import com.spoticket.teamstadium.domain.Seat;
import java.util.UUID;

public record SeatListReadResponse(
    UUID seatId,
    String section,
    Long quantity,
    Integer price
) {

  public static SeatListReadResponse from(Seat seat) {
    return new SeatListReadResponse(
        seat.getSeatId(),
        seat.getSection(),
        seat.getQuantity(),
        seat.getPrice()
    );
  }
}
