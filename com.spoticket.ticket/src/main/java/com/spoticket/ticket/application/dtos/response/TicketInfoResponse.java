package com.spoticket.ticket.application.dtos.response;

import com.spoticket.ticket.domain.entity.TicketStatus;
import java.util.UUID;

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

}
