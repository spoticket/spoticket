package com.spoticket.game.application.service;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.repository.GameRepository;
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

  private final GameRepository gameRepository;

  public GameResponse getGame(UUID gameId) {
    Game game = gameRepository.findByGameIdAndIsDeleteFalse(gameId)
        .orElseThrow(() -> new CustomException(NOT_FOUND));
    return GameResponse.from(game);
  }

  public GenericPagedModel<GameResponse> getGamesByStadiumId(UUID stadiumId, Pageable pageable) {
    Page<GameResponse> page = gameRepository.findAllByStadiumIdAndIsDeleteFalse(stadiumId,
        pageable);
    return GenericPagedModel.of(page);
  }

}
