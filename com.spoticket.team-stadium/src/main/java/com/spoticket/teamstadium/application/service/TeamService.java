package com.spoticket.teamstadium.application.service;

import com.spoticket.teamstadium.application.dto.request.TeamCreateRequest;
import com.spoticket.teamstadium.application.dto.response.GameReadResponse;
import com.spoticket.teamstadium.application.dto.response.TeamInfoResponse;
import com.spoticket.teamstadium.application.dto.response.TeamReadResponse;
import com.spoticket.teamstadium.domain.model.Team;
import com.spoticket.teamstadium.domain.repository.FavTeamRepository;
import com.spoticket.teamstadium.domain.repository.TeamRepository;
import com.spoticket.teamstadium.exception.BusinessException;
import com.spoticket.teamstadium.exception.ErrorCode;
import com.spoticket.teamstadium.exception.NotFoundException;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import com.spoticket.teamstadium.infrastructure.feign.GameServiceClient;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

  private final TeamRepository teamRepository;
  private final FavTeamRepository favTeamRepository;
  private final GameServiceClient gameServiceClient;

  // 팀 정보 등록
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
//    ApiResponse<List<GameReadResponse>> gameResponse = gameServiceClient.getGamesByTeamId(teamId);
//    if (gameResponse.code() != 200) {
//      throw new BusinessException(ErrorCode.GAME_DATA_FETCH_FAILED);
//    }
//    List<GameReadResponse> games = gameResponse.data();
    List<GameReadResponse> games = null;
    TeamReadResponse response = new TeamReadResponse(teamInfo, games);
    return new ApiResponse<>(200, "조회 완료", response);
  }

  public Team getTeamById(UUID teamId) {
    return teamRepository.findByTeamIdAndIsDeletedFalse(teamId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.TEAM_NOT_FOUND));
  }
}
