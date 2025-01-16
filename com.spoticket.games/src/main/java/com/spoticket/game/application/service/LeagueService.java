package com.spoticket.game.application.service;

import static com.spoticket.game.global.entity.UserRoleEnum.ROLE_ADMIN;
import static com.spoticket.game.global.entity.UserRoleEnum.ROLE_MASTER;

import com.spoticket.game.domain.model.League;
import com.spoticket.game.domain.model.LeagueGame;
import com.spoticket.game.domain.model.LeagueTeam;
import com.spoticket.game.domain.model.Sport;
import com.spoticket.game.domain.model.TeamScore;
import com.spoticket.game.domain.repository.LeagueJpaRepository;
import com.spoticket.game.domain.repository.LeagueTeamJpaRepository;
import com.spoticket.game.domain.repository.TeamScoreJpaRepository;
import com.spoticket.game.dto.request.CreateLeagueRequest;
import com.spoticket.game.dto.request.UpdateLeagueRequest;
import com.spoticket.game.dto.response.GenericPagedModel;
import com.spoticket.game.dto.response.ReadLeagueListResponse;
import com.spoticket.game.dto.response.ReadLeagueResponse;
import com.spoticket.game.global.exception.CustomException;
import com.spoticket.game.global.util.RequestUtils;
import com.spoticket.game.global.util.ResponseUtils.DataResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class LeagueService {

  private final LeagueJpaRepository leagueJpaRepository;
  private final LeagueTeamJpaRepository leagueTeamJpaRepository;
  private final TeamScoreJpaRepository teamScoreJpaRepository;
  private final ResultService resultService;


  // 리그 정보 등록
  public DataResponse<Map<String, UUID>> createLeague(
      CreateLeagueRequest request
  ) {
    validateUserHasAdminOrMasterRole();
    if (leagueJpaRepository.findByNameAndSeasonAndIsDeletedFalse(
        request.name(), request.season()).isPresent()) {
      throw new CustomException(400, "이미 등록되어있는 리그입니다");
    }

    League league = League.create(
        request.name(),
        request.sport(),
        request.season(),
        request.startAt(),
        request.endAt()
    );
    League savedLeague = leagueJpaRepository.save(league);
    Map<String, UUID> response = Map.of("leagueId", savedLeague.getLeagueId());
    return new DataResponse<>(200, "등록 완료", response);
  }

  // 단일 리그 조회
  public DataResponse<ReadLeagueResponse> readLeague(UUID leagueId) {
    League league = findById(leagueId);
    ReadLeagueResponse response = ReadLeagueResponse.from(league);
    return new DataResponse<>(200, "조회 완료", response);
  }

  // 리그 목록 조회
  public DataResponse<GenericPagedModel<ReadLeagueListResponse>> readLeagueList(
      Sport sport,
      int page,
      int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<League> leagues = (sport != null)
        ? leagueJpaRepository.findAllBySportAndIsDeletedFalse(sport, pageable)
        : leagueJpaRepository.findAllByIsDeletedFalse(pageable);
    Page<ReadLeagueListResponse> response = leagues.map(ReadLeagueListResponse::from);
    return new DataResponse<>(200, "조회 완료", GenericPagedModel.of(response));
  }

  // 리그 수정
  public DataResponse<Void> updateLeague(UUID leagueId, UpdateLeagueRequest request) {
    validateUserHasAdminOrMasterRole();
    League league = findById(leagueId);
    validateLeagueNameNotDuplicated(request.name(), leagueId);

    league.update(request.name(), request.startAt(), request.endAt());
    leagueJpaRepository.save(league);
    return new DataResponse<>(200, "수정 완료", null);
  }

  // 리그 삭제
  public DataResponse<Void> deleteLeague(UUID leagueId) {
    validateUserHasAdminOrMasterRole();
    League league = findById(leagueId);
    league.delete(RequestUtils.getCurrentUserId());
    leagueJpaRepository.save(league);
    return new DataResponse<>(200, "삭제 완료", null);
  }

  // LeagueTeam 업데이트
  @Transactional
  public void updateRankings() {
    List<League> activeLagues = leagueJpaRepository.findAllBySeasonAndEndAtAfterAndIsDeletedFalse(
        LocalDate.now().getYear(), LocalDate.now()
    );

    for (League league : activeLagues) {
      List<LeagueGame> yesterdayGames = resultService.findGamesByLeagueAndDate(
          league.getLeagueId(), LocalDate.now().minusDays(1)
      );

      for (LeagueGame game : yesterdayGames) {
        updateTeamStats(game);
      }

      calculateAndSaveRankings(league);
    }
  }

  private void updateTeamStats(LeagueGame game) {
    League league = findById(game.getLeague().getLeagueId());
    LeagueTeam homeTeam = findLeagueTeamByTeamIdAndLeagueId(game.getGame().getHomeTeamId(),
        league.getLeagueId()
    );
    LeagueTeam awayTeam = findLeagueTeamByTeamIdAndLeagueId(game.getGame().getAwayTeamId(),
        league.getLeagueId()
    );

    int homeScore = game.getHomeScore();
    int awayScore = game.getAwayScore();

    int winPoint = 3;
    int defeatPoint = 0;
    if (homeScore > awayScore) {
      homeTeam.updateStats(winPoint, homeScore, awayScore, true, false, false);
      awayTeam.updateStats(defeatPoint, awayScore, homeScore, false, true, false);
    } else if (game.getHomeScore() < game.getAwayScore()) {
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
        teamScore = teamScore.toBuilder()
            .currentWinRate(winRate)
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
      TeamScore teamScore = teamScoreJpaRepository.findByTeamIdAndIsDeletedFalse(team.getTeamId())
          .orElseThrow(
              () -> new RuntimeException("TeamScore not found for teamId: " + team.getTeamId()));

      teamScore = teamScore.toBuilder()
          .currentRank(rank++)
          .build();

      teamScoreJpaRepository.save(teamScore);
    }
  }

  public League findById(UUID leagueId) {
    return leagueJpaRepository.findByLeagueIdAndIsDeletedFalse(leagueId)
        .orElseThrow(() -> new CustomException(404, "해당하는 리그가 없습니다"));
  }

  public LeagueTeam findLeagueTeamByTeamIdAndLeagueId(UUID teamId, UUID leagueId) {
    return leagueTeamJpaRepository.findByTeamIdAndLeagueIdAndIsDeletedFalse(
            teamId, leagueId)
        .orElseThrow(() -> new CustomException(404, "해당하는 팀의 기록이 없습니다"));
  }

  private void validateUserHasAdminOrMasterRole() {
    if (RequestUtils.getCurrentUserRole() != ROLE_MASTER
        && RequestUtils.getCurrentUserRole() != ROLE_ADMIN) {
      throw new CustomException(403, "권한이 없습니다");
    }
  }

  private void validateLeagueNameNotDuplicated(String name, UUID leagueId) {
    if (name != null && leagueJpaRepository.findByNameAndIsDeletedFalse(name)
        .filter(existingLeague -> !existingLeague.getLeagueId().equals(leagueId))
        .isPresent()) {
      throw new CustomException(400, "중복된 리그명입니다");
    }
  }
}
