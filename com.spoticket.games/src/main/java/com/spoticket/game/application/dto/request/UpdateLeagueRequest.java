package com.spoticket.game.application.dto.request;

import java.time.LocalDate;

public record UpdateLeagueRequest(
    String name,
    LocalDate startAt,
    LocalDate endAt
) {

}
