package com.spoticket.game.application.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.repository.GameJpaRepository;
import com.spoticket.game.domain.repository.GameQueryRepository;
import com.spoticket.game.dto.request.SearchCondition;
import com.spoticket.game.dto.response.GameResponse;
import com.spoticket.game.dto.response.GenericPagedModel;
import com.spoticket.game.global.exception.CustomException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameQueryService {

  private final GameJpaRepository gameJpaRepository;
  private final GameQueryRepository gameQueryRepository;

  @Transactional
  public GameResponse getGame(UUID gameId) {
    Game game = gameJpaRepository.findByGameIdAndIsDeletedFalse(gameId)
        .orElseThrow(() -> new CustomException(NOT_FOUND));
    game.increaseHit();
    return GameResponse.from(game);
  }

  public GenericPagedModel<GameResponse> getGames(SearchCondition condition) {
    return GenericPagedModel.of(gameQueryRepository.getGames(condition));
  }

}
