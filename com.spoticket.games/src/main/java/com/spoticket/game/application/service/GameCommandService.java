package com.spoticket.game.application.service;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.repository.GameRepository;
import com.spoticket.game.dto.request.CreateGameRequest;
import com.spoticket.game.dto.request.UpdateGameRequest;
import com.spoticket.game.dto.response.GameResponse;
import com.spoticket.game.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class GameCommandService {

    private final GameRepository gameRepository;

    public GameResponse createGame(CreateGameRequest request) {
        Game game = Game.of(
                request.getTitle(), request.getStartTime(), request.getSport(), request.getLeague(),
                request.getStadiumId(), request.getHomeTeamId(), request.getAwayTeamId()
        );
        return GameResponse.from(gameRepository.save(game));
    }

    public GameResponse updateGame(UUID gameId, UpdateGameRequest request) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));
        game.update(
                request.getTitle(),
                request.getStartTime(),
                request.getLeague(),
                request.getSport(),
                request.getStadiumId(),
                request.getHomeTeamId(),
                request.getAwayTeamId()
        );
        return GameResponse.from(game);
    }

    public void deleteGame(UUID gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new CustomException(NOT_FOUND));
        game.delete();
    }

}
