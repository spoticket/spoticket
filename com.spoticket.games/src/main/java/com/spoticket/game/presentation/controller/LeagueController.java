package com.spoticket.game.presentation.controller;

import com.spoticket.game.application.service.LeagueService;
import com.spoticket.game.domain.model.Sport;
import com.spoticket.game.dto.request.CreateLeagueRequest;
import com.spoticket.game.dto.response.GenericPagedModel;
import com.spoticket.game.dto.response.ReadLeagueListResponse;
import com.spoticket.game.dto.response.ReadLeagueResponse;
import com.spoticket.game.global.util.ResponseUtils.DataResponse;
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
@RequestMapping("/api/v1/leagues")
@RequiredArgsConstructor
public class LeagueController {

  private final LeagueService leagueService;

  // 리그 등록
  @PostMapping
  public DataResponse<Map<String, UUID>> createLeague(
      @Valid @RequestBody CreateLeagueRequest request
  ) {
    return leagueService.createLeague(request);
  }

  // 단일 리그 조회
  @GetMapping("/{leagueId}")
  public DataResponse<ReadLeagueResponse> readLeague(
      @PathVariable UUID leagueId
  ) {
    return leagueService.readLeague(leagueId);
  }

  // 리그 목록 조회
  @GetMapping
  public DataResponse<GenericPagedModel<ReadLeagueListResponse>> readLeagueList(
      @RequestParam(required = false) Sport sport,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    return leagueService.readLeagueList(sport, page, size);
  }

}
