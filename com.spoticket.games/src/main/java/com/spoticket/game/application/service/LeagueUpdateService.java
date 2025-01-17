package com.spoticket.game.application.service;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.model.League;
import com.spoticket.game.domain.model.LeagueGame;
import com.spoticket.game.domain.model.LeagueTeam;
import com.spoticket.game.domain.model.TeamScore;
import com.spoticket.game.domain.repository.LeagueTeamJpaRepository;
import com.spoticket.game.domain.repository.ResultJpaRepository;
import com.spoticket.game.domain.repository.TeamScoreJpaRepository;
import com.spoticket.game.global.exception.CustomException;
import java.math.BigDecimal;
import java.time.LocalDate;
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
    if (homeScore > awayScore) {
      homeTeam.updateStats(winPoint, homeScore, awayScore, true, false, false);
      awayTeam.updateStats(defeatPoint, awayScore, homeScore, false, true, false);
    } else if (homeScore < awayScore) {
      homeTeam.updateStats(defeatPoint, homeScore, awayScore, false, true, false);
      awayTeam.updateStats(winPoint, awayScore, homeScore, true, false, false);
    } else {
      int drawPoint = 1;
      homeTeam.updateStats(drawPoint, homeScore, awayScore, false, false, true);
      awayTeam.updateStats(drawPoint, awayScore, homeScore, false, false, true);
    }
  }

  private void calculateAndSaveRankings(League league) {
    List<LeagueTeam> leagueTeams = leagueTeamJpaRepository
        .findAllByLeagueIdAndIsDeletedFalse(league.getLeagueId());

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
      teamScoreJpaRepository.save(teamScore);
    }

    leagueTeams.sort((a, b) -> {
      if (b.getTeamScore() != a.getTeamScore()) {
        return b.getTeamScore() - a.getTeamScore();
      }
      int goalDifferenceA = a.getTotalScore() - a.getTotalLoss();
      int goalDifferenceB = b.getTotalScore() - b.getTotalLoss();
      if (goalDifferenceB != goalDifferenceA) {
        return goalDifferenceB - goalDifferenceA;
      }
      return b.getTotalScore() - a.getTotalScore();
    });

    int rank = 1;
    for (LeagueTeam team : leagueTeams) {
      TeamScore teamScore = findByTeamId(team.getTeamId());

      teamScore = teamScore.toBuilder()
          .currentRank(rank++)
          .build();

      teamScoreJpaRepository.save(teamScore);
    }
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
