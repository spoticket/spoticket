package com.spoticket.game.application.dto.response;

import com.spoticket.game.domain.model.League;
import com.spoticket.game.domain.model.Sport;
import java.util.UUID;

public record ReadLeagueListResponse(
    UUID leagueId,
    String name,
    Integer season,
    Sport sport
) {

  public static ReadLeagueListResponse from(League league) {
    return new ReadLeagueListResponse(
        league.getLeagueId(),
        league.getName(),
        league.getSeason(),
        league.getSport()
    );
  }
}
