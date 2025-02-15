package com.spoticket.game.presentation;

import static com.spoticket.game.common.util.ApiResponse.created;
import static com.spoticket.game.common.util.ApiResponse.noContent;
import static com.spoticket.game.common.util.ApiResponse.ok;
import static com.spoticket.game.common.util.ErrorUtils.validateAndThrowIfInvalid;

import com.spoticket.game.application.dto.request.CreateGameRequest;
import com.spoticket.game.application.dto.request.SearchCondition;
import com.spoticket.game.application.dto.request.UpdateGameRequest;
import com.spoticket.game.application.dto.response.GameResponse;
import com.spoticket.game.application.service.GameCommandService;
import com.spoticket.game.application.service.GameQueryService;
import com.spoticket.game.common.util.ApiResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {

  public static final String HEADER_USER_ID = "X-User-Id";

  private final GameCommandService gameCommandService;
  private final GameQueryService gameQueryService;

  // Query
  @GetMapping("/{gameId}")
  public ApiResponse<GameResponse> getGame(@PathVariable UUID gameId) {
    return ok(gameQueryService.getGame(gameId));
  }

  @GetMapping
  public ApiResponse<PagedModel<GameResponse>> getGames(
      SearchCondition condition,
      Pageable pageable
  ) {
    return ok(gameQueryService.getGames(condition.withPageable(pageable)));
  }

  // Command
  @PostMapping
  public ApiResponse<GameResponse> createGame(
      @RequestHeader(HEADER_USER_ID) UUID userId,
      @Valid @RequestBody CreateGameRequest request,
      BindingResult result
  ) {
    validateAndThrowIfInvalid(result);
    return created(gameCommandService.createGame(userId, request));
  }

  @PatchMapping("/{gameId}")
  public ApiResponse<GameResponse> updateGame(
      @RequestHeader(HEADER_USER_ID) UUID userId,
      @PathVariable UUID gameId,
      @RequestBody UpdateGameRequest request) {
    return ok(gameCommandService.updateGame(userId, gameId, request));
  }

  @DeleteMapping("/{gameId}")
  public ApiResponse<Object> deleteGame(
      @RequestHeader(HEADER_USER_ID) UUID userId,
      @PathVariable UUID gameId) {
    gameCommandService.deleteGame(userId, gameId);
    return noContent();
  }

}
