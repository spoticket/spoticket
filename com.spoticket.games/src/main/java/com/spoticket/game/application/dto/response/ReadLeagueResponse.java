package com.spoticket.game.application.dto.response;

import com.spoticket.game.domain.model.League;
import com.spoticket.game.domain.model.Sport;
import java.time.LocalDate;
import java.util.UUID;

public record ReadLeagueResponse(
    UUID leagueId,
    String name,
    Integer season,
    LocalDate startAt,
    LocalDate endAt,
    Sport sport
) {

  public static ReadLeagueResponse from(League league) {
    return new ReadLeagueResponse(
        league.getLeagueId(),
        league.getName(),
        league.getSeason(),
        league.getStartAt(),
        league.getEndAt(),
        league.getSport()
    );
  }
}
