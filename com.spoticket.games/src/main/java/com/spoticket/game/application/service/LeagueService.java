package com.spoticket.game.application.service;

import static com.spoticket.game.application.dto.response.UserRoleEnum.ROLE_ADMIN;
import static com.spoticket.game.application.dto.response.UserRoleEnum.ROLE_MASTER;

import com.spoticket.game.application.dto.request.CreateLeagueRequest;
import com.spoticket.game.application.dto.request.UpdateLeagueRequest;
import com.spoticket.game.application.dto.response.GenericPagedModel;
import com.spoticket.game.application.dto.response.ReadLeagueListResponse;
import com.spoticket.game.application.dto.response.ReadLeagueRankListResponse;
import com.spoticket.game.application.dto.response.ReadLeagueRankResponse;
import com.spoticket.game.application.dto.response.ReadLeagueResponse;
import com.spoticket.game.common.exception.CustomException;
import com.spoticket.game.common.util.ApiResponse;
import com.spoticket.game.common.util.RequestUtils;
import com.spoticket.game.domain.model.League;
import com.spoticket.game.domain.model.LeagueTeam;
import com.spoticket.game.domain.model.Sport;
import com.spoticket.game.domain.model.TeamScore;
import com.spoticket.game.infrastructure.repository.LeagueJpaRepository;
import com.spoticket.game.infrastructure.repository.LeagueTeamJpaRepository;
import com.spoticket.game.infrastructure.repository.TeamScoreJpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class LeagueService {

  private final LeagueJpaRepository leagueJpaRepository;
  private final TeamScoreJpaRepository teamScoreJpaRepository;
  private final LeagueTeamJpaRepository leagueTeamJpaRepository;
  private final LeagueUpdateService leagueUpdateService;

  // 리그 정보 등록
  public ApiResponse<Map<String, UUID>> createLeague(
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
    return new ApiResponse<>(200, "등록 완료", response);
  }

  // 단일 리그 조회
  public ApiResponse<ReadLeagueResponse> readLeague(UUID leagueId) {
    League league = findById(leagueId);
    ReadLeagueResponse response = ReadLeagueResponse.from(league);
    return new ApiResponse<>(200, "조회 완료", response);
  }

  // 리그 목록 조회
  public ApiResponse<GenericPagedModel<ReadLeagueListResponse>> readLeagueList(
      Sport sport,
      int page,
      int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<League> leagues = (sport != null)
        ? leagueJpaRepository.findAllBySportAndIsDeletedFalse(sport, pageable)
        : leagueJpaRepository.findAllByIsDeletedFalse(pageable);
    Page<ReadLeagueListResponse> response = leagues.map(ReadLeagueListResponse::from);
    return new ApiResponse<>(200, "조회 완료", GenericPagedModel.of(response));
  }

  // 리그 수정
  public ApiResponse<Void> updateLeague(UUID leagueId, UpdateLeagueRequest request) {
    validateUserHasAdminOrMasterRole();
    League league = findById(leagueId);
    validateLeagueNameNotDuplicated(request.name(), leagueId);

    league.update(request.name(), request.startAt(), request.endAt());
    leagueJpaRepository.save(league);
    return new ApiResponse<>(200, "수정 완료", null);
  }

  // 리그 삭제
  public ApiResponse<Void> deleteLeague(UUID leagueId) {
    validateUserHasAdminOrMasterRole();
    League league = findById(leagueId);
    league.delete(RequestUtils.getCurrentUserId());
    leagueJpaRepository.save(league);
    return new ApiResponse<>(200, "삭제 완료", null);
  }

  public ReadLeagueRankResponse readLeagueRank(
      UUID leagueId, int page, int size
  ) {
    League league = findById(leagueId);

    Pageable pageable = PageRequest.of(page, size);
    Page<TeamScore> teamScores = teamScoreJpaRepository.findByLeagueIdAndTeamIdInLeagueTeams(
        leagueId, pageable);

    Page<ReadLeagueRankListResponse> rankingPage = teamScores.map(ts -> {
      LeagueTeam lt = leagueUpdateService.findLeagueTeamByTeamIdAndLeagueId(ts.getTeamId(),
          leagueId);
      return ReadLeagueRankListResponse.from(ts, lt);
    });
    GenericPagedModel<ReadLeagueRankListResponse> ranking = GenericPagedModel.of(rankingPage);
    return new ReadLeagueRankResponse(league.getName(), league.getSeason(), ranking);
  }

  // LeagueTeam 업데이트
  @Scheduled(cron = "0 0 0 * * ?")
  public void updateRankings() {
    List<League> activeLeagues = leagueJpaRepository.findAllBySeasonAndEndAtAfterAndIsDeletedFalse(
        LocalDate.now().getYear(), LocalDate.now().minusDays(1)
    );
    for (League league : activeLeagues) {
      leagueUpdateService.processLeagueUpdates(league);
    }
  }

  public League findById(UUID leagueId) {
    return leagueJpaRepository.findByLeagueIdAndIsDeletedFalse(leagueId)
        .orElseThrow(() -> new CustomException(404, "해당하는 리그가 없습니다"));
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
