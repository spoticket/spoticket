package com.spoticket.game.application.service;

import static java.time.LocalDateTime.now;

import com.spoticket.game.domain.model.Game;
import com.spoticket.game.domain.model.GameRank;
import com.spoticket.game.domain.repository.GameJpaRepository;
import com.spoticket.game.domain.repository.GameRankJpaRepository;
import com.spoticket.game.dto.response.GameResponse;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
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

  @Scheduled(cron = "0 0 * * * *")
  @Transactional
  public void calculateGameRank() {
    gameRankJpaRepository.deleteAll();
    gameRankJpaRepository.flush();
    List<Game> games = gameJpaRepository.findAllByIsDeletedFalseAndStartTimeAfter(now());
    for (Game game : games) {
      Long hits = game.getHits();
      long sales = new Random().nextInt(1000);
      double score = (hits * HITS_WEIGHT) + (sales * SALES_WEIGHT);
      gameRankJpaRepository.save(GameRank.of(game, score));
    }
  }

  public List<GameResponse> getGameRanks() {
    return gameRankJpaRepository.findAllByOrderByScoreDesc().stream()
        .map(gameRank -> {
          Game game = gameJpaRepository.findById(gameRank.getGame().getGameId()).orElseThrow();
          return GameResponse.from(game);
        })
        .toList();
  }

}
