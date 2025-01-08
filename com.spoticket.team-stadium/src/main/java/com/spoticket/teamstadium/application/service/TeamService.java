package com.spoticket.teamstadium.application.service;

import com.spoticket.teamstadium.application.dto.request.TeamCreateRequest;
import com.spoticket.teamstadium.application.dto.request.TeamUpdateRequest;
import com.spoticket.teamstadium.application.dto.response.GameReadResponse;
import com.spoticket.teamstadium.application.dto.response.TeamInfoResponse;
import com.spoticket.teamstadium.application.dto.response.TeamListReadResponse;
import com.spoticket.teamstadium.application.dto.response.TeamReadResponse;
import com.spoticket.teamstadium.domain.model.FavTeam;
import com.spoticket.teamstadium.domain.model.Team;
import com.spoticket.teamstadium.domain.model.TeamCategoryEnum;
import com.spoticket.teamstadium.domain.repository.FavTeamRepository;
import com.spoticket.teamstadium.domain.repository.TeamRepository;
import com.spoticket.teamstadium.exception.BusinessException;
import com.spoticket.teamstadium.exception.ErrorCode;
import com.spoticket.teamstadium.exception.NotFoundException;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import com.spoticket.teamstadium.global.dto.PaginatedResponse;
import com.spoticket.teamstadium.infrastructure.feign.GameServiceClient;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

  private final TeamRepository teamRepository;
  private final FavTeamRepository favTeamRepository;
  private final GameServiceClient gameServiceClient;

  // 팀 정보 등록
  @Transactional
  public ApiResponse<Map<String, UUID>> createTeam(TeamCreateRequest request) {

    // 요청자 권한 체크

    // 팀명 중복 체크
    if (teamRepository.findByNameAndIsDeletedFalse(request.name()).isPresent()) {
      throw new BusinessException(ErrorCode.DUPLICATE_TEAM_NAME);
    }

    Team team = Team.create(
        request.name(),
        request.category(),
        request.description(),
        request.profile(),
        request.homeLink(),
        request.snsLink()
    );

    Team savedTeam = teamRepository.save(team);
    Map<String, UUID> response = Map.of("teamId", savedTeam.getTeamId());

    return new ApiResponse<>(200, "생성 완료", response);
  }

  // 팀 정보 단일 조회
  public ApiResponse<TeamReadResponse> getTeamInfo(UUID teamId) {

    Team team = getTeamById(teamId);
    long favCnt = favTeamRepository.countByTeam_TeamId(teamId);
    // 요청자 id로 fav 등록 여부 체크 필요
    boolean isFav = false; // 임시 데이터
    TeamInfoResponse teamInfo = TeamInfoResponse.from(team, favCnt, isFav);
    // 팀 관련 게임 정보 조회 메서드 호출 필요
    ApiResponse<List<GameReadResponse>> gameResponse = gameServiceClient.getGamesByTeamId(teamId);
    if (gameResponse.code() != 200) {
      throw new BusinessException(ErrorCode.GAME_DATA_FETCH_FAILED);
    }
    List<GameReadResponse> games = gameResponse.data();
    TeamReadResponse response = new TeamReadResponse(teamInfo, games);
    return new ApiResponse<>(200, "조회 완료", response);
  }

  // 팀 목록 조회
  public PaginatedResponse<TeamListReadResponse> getTeams(
      TeamCategoryEnum category,
      Boolean fav,
      UUID userId,
      int page,
      int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Team> teams;
    if (Boolean.TRUE.equals(fav) && userId != null) {
      teams = getFavTeams(userId, pageable);
    } else if (category != null) {
      teams = teamRepository.findAllByCategoryAndIsDeletedFalse(category, pageable);
    } else {
      // 전체 조회
      teams = teamRepository.findAllByIsDeletedFalse(pageable);
    }

    Page<TeamListReadResponse> response = teams.map(TeamListReadResponse::from);
    return PaginatedResponse.of(response);
  }

  // 팀 정보 수정
  @Transactional
  public ApiResponse<TeamUpdateRequest> updateTeam(
      UUID teamId,
      TeamUpdateRequest request
  ) {
    // 요청자 권한 체크 필요

    Team team = getTeamById(teamId);
    if (request.name() != null &&
        teamRepository.findByNameAndIsDeletedFalse(request.name())
            .filter(existingTeam -> !existingTeam.getTeamId().equals(teamId))
            .isPresent()) {
      throw new BusinessException(ErrorCode.DUPLICATE_TEAM_NAME);
    }
    team.update(
        request.name(), request.description(),
        request.profile(), request.homeLink(),
        request.snsLink()
    );
    teamRepository.save(team);
    return new ApiResponse<>(200, "수정 완료", null);
  }

  // 팀 정보 삭제
  @Transactional
  public ApiResponse<Void> deleteTeam(UUID teamId) {
    // 요청자 권한 체크 필요

    Team team = getTeamById(teamId);

    List<FavTeam> favTeams = favTeamRepository.findAllByTeamId(teamId);
    favTeamRepository.deleteAll(favTeams);

    team.deleteBase();
    teamRepository.save(team);

    return new ApiResponse<>(200, "삭제 완료", null);
  }

  // 관심 팀 추가/삭제
  @Transactional
  public ApiResponse<Void> favTeam(UUID teamId) {
    // 요청자 id 추출 필요
    UUID userId = UUID.fromString("6844ee91-b725-4606-b06a-df7c7a58e452");// 임시값
    Team team = getTeamById(teamId);
    boolean isAdded;

    FavTeam favTeam = favTeamRepository.findByUserIdAndTeam_TeamId(userId, teamId).orElse(null);
    if (favTeam == null) {
      favTeam = FavTeam.create(userId, team);
      favTeamRepository.save(favTeam);
      isAdded = true;
    } else {
      favTeamRepository.delete(favTeam);
      isAdded = false;
    }
    String message = isAdded ? "관심 팀에 추가되었습니다" : "관심 팀에서 삭제되었습니다";
    return new ApiResponse<>(200, message, null);
  }

  public Team getTeamById(UUID teamId) {
    return teamRepository.findByTeamIdAndIsDeletedFalse(teamId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.TEAM_NOT_FOUND));
  }

  public Page<Team> getFavTeams(UUID userId, Pageable pageable) {
    Page<FavTeam> favTeams = favTeamRepository.findAllByUserId(userId, pageable);
    List<UUID> teamIds = favTeams.stream()
        .map(favTeam -> favTeam.getTeam().getTeamId())
        .toList();
    if (teamIds.isEmpty()) {
      return Page.empty(pageable);
    }
    return teamRepository.findAllByTeamIdInAndIsDeletedFalse(teamIds, pageable);
  }

}
