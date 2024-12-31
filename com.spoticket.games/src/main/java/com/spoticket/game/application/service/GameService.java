package com.spoticket.game.application.service;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.repository.GameRepository;
import com.spoticket.game.dto.request.CreateGameRequest;
import com.spoticket.game.dto.response.GameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameService {

    private final GameRepository gameRepository;

    @Transactional
    public GameResponse createGame(CreateGameRequest request) {
        Game game = Game.of(
                request.getTitle(), request.getStartTime(), request.getSport(), request.getLeague(),
                request.getStadiumId(), request.getHomeTeamId(), request.getAwayTeamId()
        );
        return GameResponse.from(gameRepository.save(game));
    }
}

