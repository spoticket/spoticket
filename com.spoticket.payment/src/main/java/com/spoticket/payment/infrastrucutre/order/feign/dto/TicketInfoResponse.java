package com.spoticket.payment.infrastrucutre.order.feign.dto;

import com.spoticket.payment.infrastrucutre.order.feign.ticket.TicketStatus;
import java.util.UUID;


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
){
}
