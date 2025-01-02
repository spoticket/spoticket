package com.spoticket.ticket.application.dtos.response;

import com.spoticket.ticket.domain.entity.TicketStatus;
import java.util.UUID;

public record TicketResponse(
    UUID ticketId,
    UUID userId,
    UUID gameId,
    UUID seatId,
    String seatName,
    TicketStatus status
) {

}
