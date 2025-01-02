package com.spoticket.teamstadium.presentation;

import com.spoticket.teamstadium.application.dto.request.TeamCreateRequest;
import com.spoticket.teamstadium.application.dto.response.TeamListReadResponse;
import com.spoticket.teamstadium.application.dto.response.TeamReadResponse;
import com.spoticket.teamstadium.application.service.TeamService;
import com.spoticket.teamstadium.domain.model.TeamCategoryEnum;
import com.spoticket.teamstadium.global.dto.ApiResponse;
import com.spoticket.teamstadium.global.dto.PaginatedResponse;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

  private final TeamService teamService;

  // 팀 정보 등록
  @PostMapping
  public ApiResponse<Map<String, UUID>> createTeam(
      @Valid @RequestBody TeamCreateRequest request
  ) {
    return teamService.createTeam(request);
  }

  // 팀 정보 단일 조회
  @GetMapping("/{teamId}")
  public ApiResponse<TeamReadResponse> getTeamInfo(
      @PathVariable UUID teamId
  ) {
    return teamService.getTeamInfo(teamId);
  }

  // 팀 목록 조회
  @GetMapping
  public ApiResponse<PaginatedResponse<TeamListReadResponse>> getTeams(
      @RequestParam(required = false) TeamCategoryEnum category,
      @RequestParam(required = false) Boolean fav,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
//      @RequestHeader(name = "userId", required = false) UUID userId
  ) {
    UUID userId = UUID.randomUUID(); // 임시값
    PaginatedResponse<TeamListReadResponse> response = teamService
        .getTeams(category, fav, userId, page, size);
    return new ApiResponse<>(200, "조회 완료", response);
  }
}
