package com.spoticket.game.application.dto.request;

import com.spoticket.game.domain.model.Sport;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateGameRequest(
    String title,
    @Future LocalDateTime startTime,
    Sport sport,
    UUID leagueId,
    UUID stadiumId,
    UUID homeTeamId,
    UUID awayTeamId
) {
  
}