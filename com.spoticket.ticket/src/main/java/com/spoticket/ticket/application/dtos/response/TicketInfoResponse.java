package com.spoticket.ticket.application.dtos.response;

import com.spoticket.ticket.domain.entity.Ticket;
import com.spoticket.ticket.domain.entity.TicketStatus;
import java.util.UUID;
import lombok.Builder;

@Builder
public record TicketInfoResponse(
    UUID ticketId,
    UUID userId,
    UUID gameId,
    UUID stadiumId,
    UUID seatId,
    String userName,
    String gameName,
    String stadiumName,
    String section,
    String seatName,
    Integer price,
    TicketStatus status
) {

  public static TicketInfoResponse from(Ticket ticket, String userName, String gameTitle
      , String stadiumName, String section, Integer price) {
    return TicketInfoResponse.builder()
        .ticketId(ticket.getTicketId())
        .userId(ticket.getUserId())
        .gameId(ticket.getTicketId())
        .stadiumId(ticket.getStadiumId())
        .seatId(ticket.getSeatId())
        .userName(userName)
        .gameName(gameTitle)
        .stadiumName(stadiumName)
        .section(section)
        .seatName(ticket.getSeatName())
        .price(price)
        .status(ticket.getStatus())
        .build();
  }
}
