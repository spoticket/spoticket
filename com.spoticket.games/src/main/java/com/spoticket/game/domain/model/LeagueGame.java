package com.spoticket.game.domain.model;

import com.spoticket.game.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity(name = "p_league_games")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class LeagueGame extends BaseEntity {

  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(updatable = false, nullable = false)
  private UUID leagueGameId;

  @Min(0)
  @Column(nullable = false)
  private int homeScore;

  @Min(0)
  @Column(nullable = false)
  private int awayScore;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "league_id", nullable = false)
  private League league;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "game_id", nullable = false)
  private Game game;

  public static LeagueGame create(
      Game game,
      League league,
      int homeScore,
      int awayScore
  ) {
    return LeagueGame.builder()
        .game(game)
        .league(league)
        .homeScore(homeScore)
        .awayScore(awayScore)
        .build();
  }

  public void update(Integer homeScore, Integer awayScore) {
    if (homeScore != null && homeScore >= 0) {
      this.homeScore = homeScore;
    }
    if (awayScore != null && awayScore >= 0) {
      this.awayScore = awayScore;
    }
  }
}
