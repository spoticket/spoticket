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
    String seatName,
    String stadiumName,
    TicketStatus status
) {

  public static TicketInfoResponse from(Ticket ticket, String userName, String gameTitle,
      String stadiumName) {
    return TicketInfoResponse.builder()
        .ticketId(ticket.getTicketId())
        .userId(ticket.getUserId())
        .gameId(ticket.getTicketId())
        .stadiumId(ticket.getStadiumId())
        .seatId(ticket.getSeatId())
        .userName(userName)
        .gameName(gameTitle)
        .stadiumName(stadiumName)
        .seatName(ticket.getSeatName())
        .status(ticket.getStatus())
        .build();
  }
}
