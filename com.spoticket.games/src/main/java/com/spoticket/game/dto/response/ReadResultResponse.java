package com.spoticket.game.dto.response;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.model.League;
import com.spoticket.game.domain.model.LeagueGame;
import java.util.UUID;

public record ReadResultResponse(
    UUID leagueId,
    String leagueName,
    UUID gameId,
    String gameTitle,
    UUID homeTeamId,
    UUID awayTeamId,
    int homeScore,
    int awayScore
) {

  public static ReadResultResponse from(LeagueGame lg, League league, Game game) {
    return new ReadResultResponse(
        league.getLeagueId(),
        league.getName(),
        game.getGameId(),
        game.getTitle(),
        game.getHomeTeamId(),
        game.getAwayTeamId(),
        lg.getHomeScore(),
        lg.getAwayScore()
    );
  }
}
