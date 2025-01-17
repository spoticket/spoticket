package com.spoticket.game.presentation;

import com.spoticket.game.application.dto.request.CreateResultRequest;
import com.spoticket.game.application.dto.request.UpdateLeagueGameRequest;
import com.spoticket.game.application.dto.response.GenericPagedModel;
import com.spoticket.game.application.dto.response.ReadLeagueGameListResponse;
import com.spoticket.game.application.dto.response.ReadResultResponse;
import com.spoticket.game.application.service.ResultService;
import com.spoticket.game.common.util.ApiResponse;
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
@RequestMapping("/api/v1/results")
@RequiredArgsConstructor
public class ResultController {

  private final ResultService resultService;

  @PostMapping
  public ApiResponse<Map<String, UUID>> createResult(
      @Valid @RequestBody CreateResultRequest request
  ) {
    return resultService.createResult(request);
  }

  @GetMapping("/{leagueGameId}")
  public ApiResponse<ReadResultResponse> readResult(
      @PathVariable UUID leagueGameId
  ) {
    return resultService.readResult(leagueGameId);
  }

  @GetMapping
  public ApiResponse<GenericPagedModel<ReadLeagueGameListResponse>> readResultList(
      @RequestParam(required = false) UUID league,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    return resultService.readResultList(league, page, size);
  }

  @PatchMapping("/{leagueGameId}")
  public ApiResponse<Void> updateResult(
      @PathVariable UUID leagueGameId,
      @Valid @RequestBody UpdateLeagueGameRequest request
  ) {
    return resultService.updateResult(leagueGameId, request);
  }

  @DeleteMapping("/{leagueGameId}")
  public ApiResponse<Void> deleteResult(
      @PathVariable UUID leagueGameId
  ) {
    return resultService.deleteResut(leagueGameId);
  }
}
