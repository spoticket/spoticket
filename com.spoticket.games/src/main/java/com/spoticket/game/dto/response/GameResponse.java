package com.spoticket.game.dto.response;

import com.querydsl.core.annotations.QueryProjection;
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
        .league(game.getLeague().getName())
        .stadiumId(game.getStadiumId())
        .homeTeamId(game.getHomeTeamId())
        .awayTeamId(game.getAwayTeamId())
        .build();
  }

  @QueryProjection
  public GameResponse(UUID gameId, String title, LocalDateTime startTime, Sport sport,
      String league,
      UUID stadiumId, UUID homeTeamId, UUID awayTeamId) {
    this.gameId = gameId;
    this.title = title;
    this.startTime = startTime;
    this.sport = sport;
    this.league = league;
    this.stadiumId = stadiumId;
    this.homeTeamId = homeTeamId;
    this.awayTeamId = awayTeamId;
  }

}
