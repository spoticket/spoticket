package com.spoticket.game.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "p_game_ranks")
@Builder
public class GameRank {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID gameRankId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "game_id")
  private Game game;

  private double score;

  public static GameRank of(Game game, double score) {
    return GameRank.builder()
        .game(game)
        .score(score)
        .build();
  }

}
