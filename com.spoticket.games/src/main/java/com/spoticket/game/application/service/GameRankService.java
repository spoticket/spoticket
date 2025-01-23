package com.spoticket.game.application.service;

import static java.lang.Integer.MAX_VALUE;
import static java.time.LocalDateTime.now;

import com.spoticket.game.application.dto.response.GameResponse;
import com.spoticket.game.application.dto.response.TicketStatus;
import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.model.GameRank;
import com.spoticket.game.infrastructure.client.TicketServiceClient;
import com.spoticket.game.infrastructure.repository.GameJpaRepository;
import com.spoticket.game.infrastructure.repository.GameRankJpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GameRankService {

  private static final double HITS_WEIGHT = 0.6;
  private static final double SALES_WEIGHT = 0.4;

  private final GameJpaRepository gameJpaRepository;
  private final GameRankJpaRepository gameRankJpaRepository;
  private final TicketServiceClient ticketServiceClient;

  public List<GameResponse> getGameRanks() {
    return gameRankJpaRepository.findAllByOrderByScoreDesc().stream()
        .map(gameRank -> {
          Game game = gameJpaRepository.findById(gameRank.getGame().getGameId()).orElseThrow();
          return GameResponse.from(game);
        })
        .toList();
  }

  @Scheduled(cron = "0 0 * * * *")
  @Transactional
  public void calculateGameRank() {
    gameRankJpaRepository.deleteAll();
    gameRankJpaRepository.flush();

    List<Game> games = gameJpaRepository.findAllByIsDeletedFalseAndStartTimeAfter(now());
    List<Long> hitsList = games.stream().map(Game::getHits).toList();
    List<Long> salesList = games.stream().map(game -> getSales(game.getGameId())).toList();

    long hitsMin = hitsList.stream().min(Long::compare).orElse(0L);
    long hitsMax = hitsList.stream().max(Long::compare).orElse(1L);
    long salesMin = salesList.stream().min(Long::compare).orElse(0L);
    long salesMax = salesList.stream().max(Long::compare).orElse(1L);

    for (int i = 0; i < games.size(); i++) {
      Game game = games.get(i);
      double normalizedHits = normalize(hitsList.get(i), hitsMin, hitsMax);
      double normalizedSales = normalize(salesList.get(i), salesMin, salesMax);
      double score = (normalizedHits * HITS_WEIGHT) + (normalizedSales * SALES_WEIGHT);
      gameRankJpaRepository.save(GameRank.of(game, score));
    }
  }

  private double normalize(long value, long min, long max) {
    if (max == min) {
      return 0.0;
    }
    return (double) (value - min) / (max - min);
  }

  private Long getSales(UUID gameId) {
    return Optional.ofNullable(
            ticketServiceClient
                .getTicketsByGameId(gameId, TicketStatus.BOOKED, MAX_VALUE)
                .data()
        )
        .map(Page::getTotalElements)
        .orElse(0L);
  }

}
