package com.spoticket.ticket.application.dtos.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateTicketRequest(
    @NotNull UUID gameId,
    @NotNull UUID stadiumId,
    @NotNull UUID seatId,
    @NotNull String seatName
) {

}
