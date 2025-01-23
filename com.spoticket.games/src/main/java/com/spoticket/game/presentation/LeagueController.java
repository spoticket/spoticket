package com.spoticket.game.presentation;

import com.spoticket.game.application.dto.request.CreateLeagueRequest;
import com.spoticket.game.application.dto.request.UpdateLeagueRequest;
import com.spoticket.game.application.dto.response.GenericPagedModel;
import com.spoticket.game.application.dto.response.ReadLeagueListResponse;
import com.spoticket.game.application.dto.response.ReadLeagueRankResponse;
import com.spoticket.game.application.dto.response.ReadLeagueResponse;
import com.spoticket.game.application.service.LeagueService;
import com.spoticket.game.common.util.ApiResponse;
import com.spoticket.game.domain.model.Sport;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/leagues")
@RequiredArgsConstructor
public class LeagueController {

  private final LeagueService leagueService;

  // 리그 등록
  @PostMapping
  public ApiResponse<Map<String, UUID>> createLeague(
      @Valid @RequestBody CreateLeagueRequest request
  ) {
    return leagueService.createLeague(request);
  }

  // 단일 리그 조회
  @GetMapping("/{leagueId}")
  public ApiResponse<ReadLeagueResponse> readLeague(
      @PathVariable UUID leagueId
  ) {
    return leagueService.readLeague(leagueId);
  }

  // 리그 목록 조회
  @GetMapping
  public ApiResponse<GenericPagedModel<ReadLeagueListResponse>> readLeagueList(
      @RequestParam(required = false) Sport sport,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    return leagueService.readLeagueList(sport, page, size);
  }

  // 리그 수정
  @PatchMapping("/{leagueId}")
  public ApiResponse<Void> updateLeague(
      @PathVariable UUID leagueId,
      @Valid @RequestBody UpdateLeagueRequest request
  ) {
    return leagueService.updateLeague(leagueId, request);
  }

  // 리그 삭제
  @DeleteMapping("/{leagueId}")
  public ApiResponse<Void> deleteLeague(
      @PathVariable UUID leagueId
  ) {
    return leagueService.deleteLeague(leagueId);
  }

  // 리그별 랭킹 조회
  @GetMapping("/{leagueId}/rank")
  public ApiResponse<ReadLeagueRankResponse> readLeagueRank(
      @PathVariable UUID leagueId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    ReadLeagueRankResponse response = leagueService.readLeagueRank(leagueId, page, size);
    return new ApiResponse<>(200, "조회 완료", response);
  }

}
