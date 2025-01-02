package com.spoticket.teamstadium.application.service;

import com.spoticket.teamstadium.application.dto.request.TeamCreateRequest;
import com.spoticket.teamstadium.domain.model.Team;
import com.spoticket.teamstadium.domain.repository.TeamRepository;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

  private final TeamRepository teamRepository;

  // 팀 정보 등록
  public ApiResponse<Map<String, UUID>> createTeam(TeamCreateRequest request) {

    // 요청자 권한 체크

    // 팀명 중복 체크
    if (teamRepository.findByName(request.name()).isPresent()) {
      return new ApiResponse<>(400, "중복된 팀명입니다", null);
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

}
