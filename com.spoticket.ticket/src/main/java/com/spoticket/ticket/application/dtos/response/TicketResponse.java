package com.spoticket.ticket.application.dtos.response;

import com.spoticket.ticket.domain.entity.Ticket;
import com.spoticket.ticket.domain.entity.TicketStatus;
import java.io.Serializable;
import java.util.UUID;
import lombok.Builder;

@Builder
public record TicketResponse(
    UUID ticketId,
    UUID userId,
    UUID gameId,
    UUID stadiumId,
    UUID seatId,
    String seatName,
    TicketStatus status
) implements Serializable {

  public static TicketResponse from(Ticket ticket) {
    return TicketResponse.builder()
        .ticketId(ticket.getTicketId())
        .userId(ticket.getUserId())
        .gameId(ticket.getGameId())
        .stadiumId(ticket.getStadiumId())
        .seatId(ticket.getSeatId())
        .seatName(ticket.getSeatName())
        .status(ticket.getStatus())
        .build();
  }
}
