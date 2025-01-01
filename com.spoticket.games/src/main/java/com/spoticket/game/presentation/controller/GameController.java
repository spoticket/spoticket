package com.spoticket.game.presentation.controller;

import com.spoticket.game.application.service.GameService;
import com.spoticket.game.dto.request.CreateGameRequest;
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

    private final GameService gameService;

    @PostMapping
    public DataResponse<GameResponse> createGame(
            @Valid @RequestBody CreateGameRequest request,
            BindingResult result
    ) throws CustomException {
        validateAndThrowIfInvalid(result);
        return created(gameService.createGame(request));
    }

    @GetMapping("/{gameId}")
    public DataResponse<GameResponse> getGame(@PathVariable UUID gameId) {
        return ok(gameService.getGame(gameId));
    }

    @GetMapping
    public DataResponse<PagedModel<GameResponse>> getGamesByStadiumId(UUID stadiumId, Pageable pageable) {
        return ok(gameService.getGamesByStadiumId(stadiumId, pageable));
    }

    @DeleteMapping("/{gameId}")
    public BasicResponse deleteGame(@PathVariable UUID gameId) {
        gameService.deleteGame(gameId);
        return noContent();
    }

}
