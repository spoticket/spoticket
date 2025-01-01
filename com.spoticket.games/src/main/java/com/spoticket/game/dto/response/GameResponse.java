package com.spoticket.game.dto.response;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.model.Sport;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GameResponse {

  private UUID gameId;
  private String title;
  private LocalDateTime startTime;
  private Sport sport;
  private String league;
  private UUID stadiumId;
  private UUID homeTeamId;
  private UUID awayTeamId;

  public static GameResponse from(Game game) {
    return GameResponse.builder()
        .gameId(game.getGameId())
        .title(game.getTitle())
        .startTime(game.getStartTime())
        .sport(game.getSport())
        .league(game.getLeague())
        .stadiumId(game.getStadiumId())
        .homeTeamId(game.getHomeTeamId())
        .awayTeamId(game.getAwayTeamId())
        .build();
  }

}
