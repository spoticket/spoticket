package com.spoticket.game.presentation.controller;

import com.spoticket.game.application.service.ResultService;
import com.spoticket.game.dto.request.CreateResultRequest;
import com.spoticket.game.dto.response.GenericPagedModel;
import com.spoticket.game.dto.response.ReadLeagueGameListResponse;
import com.spoticket.game.dto.response.ReadResultResponse;
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
@RequestMapping("/api/v1/results")
@RequiredArgsConstructor
public class ResultController {

  private final ResultService resultService;

  @PostMapping
  public DataResponse<Map<String, UUID>> createResult(
      @Valid @RequestBody CreateResultRequest request
  ) {
    return resultService.createResult(request);
  }

  @GetMapping("/{leagueGameId}")
  public DataResponse<ReadResultResponse> readResult(
      @PathVariable UUID leagueGameId
  ) {
    return resultService.readResult(leagueGameId);
  }

  @GetMapping
  public DataResponse<GenericPagedModel<ReadLeagueGameListResponse>> readResultList(
      @RequestParam(required = false) UUID league,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    return resultService.readResultList(league, page, size);
  }
}
