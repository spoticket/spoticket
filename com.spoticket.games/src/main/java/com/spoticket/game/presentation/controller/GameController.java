package com.spoticket.game.presentation.controller;

import com.spoticket.game.application.service.GameService;
import com.spoticket.game.dto.request.CreateGameRequest;
import com.spoticket.game.dto.response.GameResponse;
import com.spoticket.game.global.exception.CustomException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
