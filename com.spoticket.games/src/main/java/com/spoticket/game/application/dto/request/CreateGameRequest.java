package com.spoticket.game.application.dto.request;

import com.spoticket.game.domain.model.Sport;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateGameRequest(
    @NotBlank String title,
    @NotNull @Future LocalDateTime startTime,
    @NotNull Sport sport,
    UUID leagueId,
    @NotNull UUID stadiumId,
    @NotNull UUID homeTeamId,
    @NotNull UUID awayTeamId
) {

}
