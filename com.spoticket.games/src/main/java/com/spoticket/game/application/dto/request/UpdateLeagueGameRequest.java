package com.spoticket.game.application.dto.request;

import jakarta.validation.constraints.Min;

public record UpdateLeagueGameRequest(
    @Min(0) Integer homeScore,
    @Min(0) Integer awayScore
) {

}
