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
    UUID seatId,
    String userName,
    String gameName,
    String section,
    String seatName,
    String stadiumName,
    TicketStatus status
) {

  public static TicketInfoResponse from(Ticket ticket, String gameTitle) {
    return TicketInfoResponse.builder()
        .ticketId(ticket.getTicketId())
        .userId(ticket.getUserId())
        .gameId(ticket.getTicketId())
        .seatId(ticket.getSeatId())
        .gameName(gameTitle)
        .seatName(ticket.getSeatName())
        .status(ticket.getStatus())
        .build();
  }
}
