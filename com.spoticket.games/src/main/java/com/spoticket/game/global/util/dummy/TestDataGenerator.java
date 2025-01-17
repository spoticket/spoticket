package com.spoticket.game.global.util.dummy;

import com.spoticket.game.common.exception.CustomException;
import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.model.League;
import com.spoticket.game.domain.model.LeagueGame;
import com.spoticket.game.domain.model.LeagueTeam;
import com.spoticket.game.domain.model.Sport;
import com.spoticket.game.domain.model.TeamScore;
import com.spoticket.game.infrastructure.repository.GameJpaRepository;
import com.spoticket.game.infrastructure.repository.LeagueJpaRepository;
import com.spoticket.game.infrastructure.repository.ResultJpaRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataGenerator {


  private final LeagueJpaRepository leagueRepository;
  private final com.spoticket.game.domain.repository.LeagueTeamJpaRepository leagueTeamRepository;
  private final GameJpaRepository gameRepository;
  private final ResultJpaRepository resultRepository;
  private final com.spoticket.game.domain.repository.TeamScoreJpaRepository teamScoreRepository;

  @Transactional
  public void generateData() {
    List<League> leagues = generateLeagues(30);
    leagues.forEach(league -> {
      List<LeagueTeam> teams = generateTeamsForLeague(league, 20);
      List<LeagueGame> games = generateGamesForLeague(league, teams);
      calculateResults(teams, games);
      generateYesterdayGames(league, teams, 10);
    });
  }

  private List<League> generateLeagues(int leagueCount) {
    List<League> leagues = new ArrayList<>();
    for (int i = 1; i <= leagueCount; i++) {
      leagues.add(League.create("League " + i, Sport.SOCCER, 2025,
          LocalDate.now(), LocalDate.now().plusMonths(6)));
    }
    return leagueRepository.saveAll(leagues);
  }

  private List<LeagueTeam> generateTeamsForLeague(League league, int teamCount) {
    List<LeagueTeam> teams = new ArrayList<>();
    for (int i = 1; i <= teamCount; i++) {
      teams.add(LeagueTeam.builder()
          .teamId(UUID.randomUUID())
          .league(league)
          .build());
    }
    return leagueTeamRepository.saveAll(teams);
  }

  private List<LeagueGame> generateGamesForLeague(League league, List<LeagueTeam> teams) {
    List<LeagueGame> games = new ArrayList<>();
    Random random = new Random();

    for (int i = 0; i < teams.size(); i++) {
      for (int j = 0; j < teams.size(); j++) {
        if (i != j) {
          for (int k = 0; k < 2; k++) {
            LocalDate gameDate = league.getStartAt()
                .plusDays(random.nextInt(
                    (int) (league.getEndAt().toEpochDay() - league.getStartAt().toEpochDay())));
            LocalDateTime gameDateTime = gameDate.atTime(
                random.nextInt(24),
                random.nextInt(60)
            );
            Game game = Game.of(
                "match: " + teams.get(i).getTeamId() + " vs " + teams.get(j).getTeamId(),
                gameDateTime,
                Sport.SOCCER, league,
                UUID.randomUUID(),
                k == 0 ? teams.get(i).getTeamId() : teams.get(j).getTeamId(),
                k == 0 ? teams.get(j).getTeamId() : teams.get(i).getTeamId()
            );
            gameRepository.save(game);
            games.add(LeagueGame.create(game, league, 0, 0));
          }
        }
      }
    }
    return resultRepository.saveAll(games);
  }

  private void generateYesterdayGames(League league, List<LeagueTeam> teams, int gameCount) {
    List<LeagueGame> yesterdayGames = new ArrayList<>();
    Random random = new Random();

    for (int i = 0; i < gameCount; i++) {
      LeagueTeam homeTeam = teams.get(random.nextInt(teams.size()));
      LeagueTeam awayTeam;
      do {
        awayTeam = teams.get(random.nextInt(teams.size()));
      } while (homeTeam.equals(awayTeam));

      LocalDateTime yesterday = LocalDate.now().minusDays(1)
          .atTime(random.nextInt(24), random.nextInt(60));

      Game game = Game.of(
          "Yesterday Match: " + homeTeam.getTeamId() + " vs " + awayTeam.getTeamId(),
          yesterday,
          Sport.SOCCER, league,
          UUID.randomUUID(),
          homeTeam.getTeamId(),
          awayTeam.getTeamId()
      );
      gameRepository.save(game);
      yesterdayGames.add(LeagueGame.create(game, league, 0, 0));
    }
    resultRepository.saveAll(yesterdayGames);
  }

  private void calculateResults(List<LeagueTeam> teams, List<LeagueGame> games) {
    Random random = new Random();

    for (LeagueGame game : games) {
      int homeScore = random.nextInt(5);
      int awayScore = random.nextInt(5);

      game.update(homeScore, awayScore);

      LeagueTeam homeTeam = findLeagueTeam(teams, game.getGame().getHomeTeamId());
      LeagueTeam awayTeam = findLeagueTeam(teams, game.getGame().getAwayTeamId());

      if (homeScore > awayScore) {
        homeTeam.updateStats(3, homeScore, awayScore, true, false, false);
        awayTeam.updateStats(0, awayScore, homeScore, false, true, false);
      } else if (homeScore < awayScore) {
        homeTeam.updateStats(0, homeScore, awayScore, false, true, false);
        awayTeam.updateStats(3, awayScore, homeScore, true, false, false);
      } else {
        homeTeam.updateStats(1, homeScore, awayScore, false, false, true);
        awayTeam.updateStats(1, awayScore, homeScore, false, false, true);
      }
    }

    calculateRankings(teams);
  }

  private void calculateRankings(List<LeagueTeam> teams) {
    teams.sort(Comparator.comparingInt(LeagueTeam::getTeamScore).reversed()
        .thenComparingInt(team -> team.getTotalScore() - team.getTotalLoss()).reversed());

    int rank = 1;
    for (LeagueTeam team : teams) {
      TeamScore teamScore = TeamScore.builder()
          .teamId(team.getTeamId())
          .currentWinRate(BigDecimal.valueOf((double) team.getWinCnt() /
              (team.getWinCnt() + team.getDrawCnt() + team.getDefeatCnt()) * 100))
          .currentRank(rank++)
          .totalWinRate(BigDecimal.ZERO)
          .build();
      teamScoreRepository.save(teamScore);
    }
  }

  private LeagueTeam findLeagueTeam(List<LeagueTeam> teams, UUID teamId) {
    return teams.stream()
        .filter(team -> team.getTeamId().equals(teamId))
        .findFirst()
        .orElseThrow(() -> new CustomException(404, "team not found : " + teamId));
  }
}
