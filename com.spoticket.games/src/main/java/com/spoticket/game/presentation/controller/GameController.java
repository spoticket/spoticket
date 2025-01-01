package com.spoticket.game.presentation.controller;

import com.spoticket.game.application.service.GameCommandService;
import com.spoticket.game.application.service.GameQueryService;
import com.spoticket.game.dto.request.CreateGameRequest;
import com.spoticket.game.dto.request.UpdateGameRequest;
import com.spoticket.game.dto.response.GameResponse;
import com.spoticket.game.global.exception.CustomException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.spoticket.game.global.util.ErrorUtils.validateAndThrowIfInvalid;
import static com.spoticket.game.global.util.ResponseUtils.*;

@RestController
@RequestMapping("/api/v1/games")
@RequiredArgsConstructor
public class GameController {

    private final GameCommandService gameCommandService;
    private final GameQueryService gameQueryService;

    @PostMapping
    public DataResponse<GameResponse> createGame(
            @Valid @RequestBody CreateGameRequest request,
            BindingResult result
    ) throws CustomException {
        validateAndThrowIfInvalid(result);
        return created(gameCommandService.createGame(request));
    }

    @GetMapping("/{gameId}")
    public DataResponse<GameResponse> getGame(@PathVariable UUID gameId) {
        return ok(gameQueryService.getGame(gameId));
    }

    @GetMapping
    public DataResponse<PagedModel<GameResponse>> getGamesByStadiumId(UUID stadiumId, Pageable pageable) {
        return ok(gameQueryService.getGamesByStadiumId(stadiumId, pageable));
    }

    @PatchMapping("/{gameId}")
    public DataResponse<GameResponse> updateGame(@PathVariable UUID gameId, @RequestBody UpdateGameRequest request) {
        return ok(gameCommandService.updateGame(gameId, request));
    }

    @DeleteMapping("/{gameId}")
    public BasicResponse deleteGame(@PathVariable UUID gameId) {
        gameCommandService.deleteGame(gameId);
        return noContent();
    }

}
