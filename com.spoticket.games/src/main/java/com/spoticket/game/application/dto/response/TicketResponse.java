package com.spoticket.game.application.dto.response;

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
