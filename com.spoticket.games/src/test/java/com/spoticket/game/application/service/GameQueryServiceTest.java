package com.spoticket.game.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.model.League;
import com.spoticket.game.domain.model.Sport;
import com.spoticket.game.domain.repository.GameJpaRepository;
import com.spoticket.game.dto.response.GameResponse;
import com.spoticket.game.global.exception.CustomException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GameQueryServiceTest {

  @Mock
  private GameJpaRepository gameJpaRepository;

  @InjectMocks
  private GameQueryService gameQueryService;

  private Game game;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    League basketballLeague = League.builder()
        .name("NBA")
        .sport(Sport.BASKETBALL)
        .season(2025)
        .build();

    game = new Game(UUID.randomUUID(), "test", LocalDateTime.now().plusHours(2),
        Sport.BASKETBALL, basketballLeague, UUID.randomUUID(), UUID.randomUUID(),
        UUID.randomUUID(), 0L);
  }

  @Test
  void getGame_success() {
    // given
    when(gameJpaRepository.findByGameIdAndIsDeletedFalse(any(UUID.class))).thenReturn(
        Optional.of(game));

    // when
    GameResponse gameResponse = gameQueryService.getGame(game.getGameId());

    // then
    assertThat(gameResponse)
        .isNotNull()
        .extracting(
            "gameId",
            "title",
            "startTime",
            "sport",
            "league",
            "stadiumId",
            "homeTeamId",
            "awayTeamId"
        )
        .containsExactly(
            game.getGameId(),
            game.getTitle(),
            game.getStartTime(),
            game.getSport(),
            game.getLeague(),
            game.getStadiumId(),
            game.getHomeTeamId(),
            game.getAwayTeamId()
        );
  }

  @Test
  void getGame_notFound() {
    // given
    when(gameJpaRepository.findByGameIdAndIsDeletedFalse(any(UUID.class))).thenReturn(
        Optional.empty());

    // when & then
    assertThatThrownBy(() -> gameQueryService.getGame(UUID.randomUUID()))
        .isInstanceOf(CustomException.class)
        .extracting("code", "msg")
        .containsExactly(NOT_FOUND.value(), NOT_FOUND.getReasonPhrase());
  }

}