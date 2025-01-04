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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameQueryService {

  private final GameJpaRepository gameJpaRepository;
  private final GameQueryRepository gameQueryRepository;

  public GameResponse getGame(UUID gameId) {
    Game game = gameJpaRepository.findByGameIdAndIsDeleteFalse(gameId)
        .orElseThrow(() -> new CustomException(NOT_FOUND));
    return GameResponse.from(game);
  }

  public GenericPagedModel<GameResponse> getGamesByStadiumId(UUID stadiumId, Pageable pageable) {
    Page<GameResponse> page = gameJpaRepository.findAllByStadiumIdAndIsDeleteFalse(stadiumId,
        pageable);
    return GenericPagedModel.of(page);
  }

  public GenericPagedModel<GameResponse> getGames(SearchCondition condition) {
    return GenericPagedModel.of(gameQueryRepository.getGames(condition));
  }

}
