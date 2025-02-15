package com.spoticket.game.application.service;

import com.spoticket.game.common.exception.CustomException;
import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.model.League;
import com.spoticket.game.domain.model.LeagueGame;
import com.spoticket.game.domain.model.LeagueTeam;
import com.spoticket.game.domain.model.TeamScore;
import com.spoticket.game.infrastructure.repository.LeagueTeamJpaRepository;
import com.spoticket.game.infrastructure.repository.ResultJpaRepository;
import com.spoticket.game.infrastructure.repository.TeamScoreJpaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeagueUpdateService {

  private final LeagueTeamJpaRepository leagueTeamJpaRepository;
  private final ResultJpaRepository resultJpaRepository;
  private final TeamScoreJpaRepository teamScoreJpaRepository;

  @Transactional
  public void processLeagueUpdates(League league) {
    List<LeagueGame> yesterdayGames = findGamesByLeagueAndDate(
        league.getLeagueId(), LocalDate.now().minusDays(1));

    for (LeagueGame game : yesterdayGames) {
      updateTeamStats(game);
    }
    calculateAndSaveRankings(league);
  }

  private void updateTeamStats(LeagueGame lg) {
    League league = lg.getLeague();
    Game game = lg.getGame();
    LeagueTeam homeTeam = findLeagueTeamByTeamIdAndLeagueId(game.getHomeTeamId(),
        league.getLeagueId()
    );
    LeagueTeam awayTeam = findLeagueTeamByTeamIdAndLeagueId(game.getAwayTeamId(),
        league.getLeagueId()
    );

    int homeScore = lg.getHomeScore();
    int awayScore = lg.getAwayScore();

    int winPoint = 3;
    int defeatPoint = 0;
    int drawPoint = 1;
    if (homeScore > awayScore) {
      homeTeam.updateStats(winPoint, homeScore, awayScore);
      homeTeam.incrementWin();

      awayTeam.updateStats(defeatPoint, awayScore, homeScore);
      awayTeam.incrementDefeat();
    } else if (homeScore < awayScore) {
      homeTeam.updateStats(defeatPoint, homeScore, awayScore);
      homeTeam.incrementDefeat();

      awayTeam.updateStats(winPoint, awayScore, homeScore);
      awayTeam.incrementWin();
    } else {
      homeTeam.updateStats(drawPoint, homeScore, awayScore);
      homeTeam.incrementDraw();

      awayTeam.updateStats(drawPoint, awayScore, homeScore);
      awayTeam.incrementDraw();
    }
  }

  private void calculateAndSaveRankings(League league) {
    List<LeagueTeam> leagueTeams = leagueTeamJpaRepository
        .findAllByLeagueIdAndIsDeletedFalse(league.getLeagueId());

    List<TeamScore> teamScores = new ArrayList<>();

    for (LeagueTeam team : leagueTeams) {
      int totalGames = team.getWinCnt() + team.getDefeatCnt() + team.getDrawCnt();

      TeamScore teamScore = teamScoreJpaRepository.findByTeamIdAndIsDeletedFalse(team.getTeamId())
          .orElseGet(() -> TeamScore.builder()
              .teamId(team.getTeamId())
              .currentWinRate(BigDecimal.ZERO)
              .totalWinRate(BigDecimal.ZERO)
              .currentRank(null)
              .build()
          );
      if (totalGames > 0) {
        BigDecimal winRate = BigDecimal.valueOf((double) team.getWinCnt() / totalGames * 100);
        BigDecimal totalWinRate = teamScore.getTotalWinRate()
            .add(BigDecimal.valueOf((double) team.getWinCnt() / totalGames * 100));
        teamScore = teamScore.toBuilder()
            .currentWinRate(winRate)
            .totalWinRate(totalWinRate)
            .build();
      }
      teamScores.add(teamScore);
    }

    Comparator<LeagueTeam> rankingComparator = Comparator
        .comparingInt(LeagueTeam::getTeamScore).reversed()
        .thenComparingInt(team -> team.getTotalScore() - team.getTotalLoss()).reversed()
        .thenComparingInt(LeagueTeam::getTotalScore).reversed();

    leagueTeams.sort(rankingComparator);

    for (int i = 0; i < leagueTeams.size(); i++) {
      LeagueTeam team = leagueTeams.get(i);

      TeamScore teamScore = findByTeamId(team.getTeamId());
      teamScore = teamScore.toBuilder()
          .currentRank(i + 1)
          .build();

      teamScores.set(i, teamScore);
    }

    teamScoreJpaRepository.saveAll(teamScores);
    leagueTeamJpaRepository.saveAll(leagueTeams);
  }

  public List<LeagueGame> findGamesByLeagueAndDate(UUID leagueId, LocalDate date) {
    return resultJpaRepository
        .findAllByLeagueIdAndDateAndIsDeletedFalse(leagueId, date);
  }


  public TeamScore findByTeamId(UUID teamId) {
    return teamScoreJpaRepository.findByTeamIdAndIsDeletedFalse(teamId)
        .orElseThrow(() -> new CustomException(404, "해당하는 팀 기록이 없습니다"));
  }

  public LeagueTeam findLeagueTeamByTeamIdAndLeagueId(UUID teamId, UUID leagueId) {
    return leagueTeamJpaRepository.findByTeamIdAndLeagueIdAndIsDeletedFalse(
            teamId, leagueId)
        .orElseThrow(() -> new CustomException(404, "해당하는 팀의 기록이 없습니다"));
  }
}
