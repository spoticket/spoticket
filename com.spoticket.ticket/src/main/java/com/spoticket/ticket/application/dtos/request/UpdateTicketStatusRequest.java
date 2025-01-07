package com.spoticket.ticket.application.dtos.request;

import com.spoticket.ticket.domain.entity.TicketStatus;

public record UpdateTicketStatusRequest(
    TicketStatus status
) {

}
