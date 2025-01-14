package com.spoticket.ticket.application.dtos.response;

import java.util.UUID;

public record GameResponse(
    UUID gameId,
    String title
) {

}
