package com.spoticket.game.application.dto.response;

import com.spoticket.game.domain.model.LeagueTeam;
import com.spoticket.game.domain.model.TeamScore;
import java.math.BigDecimal;
import java.util.UUID;

public record ReadLeagueRankListResponse(
    int rank,
    UUID teamId,
    int teamScore,
    int winCnt,
    int defeatCnt,
    int drawCnt,
    int totalScore,
    int totalLoss,
    BigDecimal currentWinRate
) {

  public static ReadLeagueRankListResponse from(TeamScore ts, LeagueTeam lt) {
    return new ReadLeagueRankListResponse(
        ts.getCurrentRank(),
        ts.getTeamId(),
        lt.getTeamScore(),
        lt.getWinCnt(),
        lt.getDefeatCnt(),
        lt.getDrawCnt(),
        lt.getTotalScore(),
        lt.getTotalLoss(),
        ts.getCurrentWinRate()
    );
  }
}
