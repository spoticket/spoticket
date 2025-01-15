package com.spoticket.game.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateResultRequest(
    @NotNull UUID gameId,
    @NotNull UUID leagueId,
    @NotNull int homeScore,
    @NotNull int awayScore
) {

}
