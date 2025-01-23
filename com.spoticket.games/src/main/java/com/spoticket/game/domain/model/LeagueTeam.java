package com.spoticket.game.domain.model;

import com.spoticket.game.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity(name = "p_league_teams")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class LeagueTeam extends BaseEntity {

  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(updatable = false, nullable = false)
  private UUID leagueTeamId;

  @Column(nullable = false)
  private int teamScore = 0;

  @Column(nullable = false)
  private int winCnt = 0;

  @Column(nullable = false)
  private int defeatCnt = 0;

  @Column(nullable = false)
  private int drawCnt = 0;

  @Column(nullable = false)
  private int totalScore = 0;

  @Column(nullable = false)
  private int totalLoss = 0;

  @Column(nullable = false)
  private UUID teamId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "league_id", nullable = false)
  private League league;

  public void updateStats(
      int points, int goalsFor, int goalsAgainst
  ) {
    this.teamScore += points;
    this.totalScore += goalsFor;
    this.totalLoss += goalsAgainst;
  }

  public void incrementWin() {
    this.winCnt++;
  }

  public void incrementDefeat() {
    this.defeatCnt++;
  }

  public void incrementDraw() {
    this.drawCnt++;
  }
}