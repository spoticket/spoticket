package com.spoticket.game.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity(name = "p_team_scores")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class TeamScore {

  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(updatable = false, nullable = false)
  private UUID teamScoreId;

  @Column(nullable = false)
  private BigDecimal currentWinRate;

  @Column(nullable = false)
  private BigDecimal totalWinRate;

  private Integer currnetRank;

  private UUID teamId;

}
