package com.spoticket.game.dto.response;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.model.LeagueGame;
import java.util.UUID;

public record ReadLeagueGameListResponse(
    UUID leagueGameId,
    String title,
    UUID homeTeamId,
    UUID awayTeamId,
    int homeScore,
    int awayScore
) {

  public static ReadLeagueGameListResponse from(
      LeagueGame lg
  ) {
    Game game = lg.getGame();
    return new ReadLeagueGameListResponse(
        lg.getLeagueGameId(),
        game.getTitle(),
        game.getHomeTeamId(),
        game.getAwayTeamId(),
        lg.getHomeScore(),
        lg.getAwayScore()
    );
  }
}
