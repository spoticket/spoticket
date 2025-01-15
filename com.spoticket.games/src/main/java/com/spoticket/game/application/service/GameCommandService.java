package com.spoticket.game.application.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.model.League;
import com.spoticket.game.domain.repository.GameJpaRepository;
import com.spoticket.game.dto.request.CreateGameRequest;
import com.spoticket.game.dto.request.UpdateGameRequest;
import com.spoticket.game.dto.response.GameResponse;
import com.spoticket.game.global.exception.CustomException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GameCommandService {

  private final GameJpaRepository gameJpaRepository;
  private final LeagueService leagueService;

  public GameResponse createGame(UUID userId, CreateGameRequest request) {
    League league = leagueService.findById(request.getLeagueId());
    Game game = Game.of(
        request.getTitle(), request.getStartTime(), request.getSport(), league,
        request.getStadiumId(), request.getHomeTeamId(), request.getAwayTeamId()
    );
    game.setCreatedBy(userId);
    return GameResponse.from(gameJpaRepository.save(game));
  }

  public GameResponse updateGame(UUID userId, UUID gameId, UpdateGameRequest request) {
    Game game = gameJpaRepository.findById(gameId)
        .orElseThrow(() -> new CustomException(NOT_FOUND));
    League league = leagueService.findById(request.getLeagueId());
    game.update(
        request.getTitle(),
        request.getStartTime(),
        league,
        request.getSport(),
        request.getStadiumId(),
        request.getHomeTeamId(),
        request.getAwayTeamId()
    );
    game.setUpdatedBy(userId);
    return GameResponse.from(game);
  }

  public void deleteGame(UUID userId, UUID gameId) {
    Game game = gameJpaRepository.findById(gameId)
        .orElseThrow(() -> new CustomException(NOT_FOUND));
    game.delete(userId);
  }

}
