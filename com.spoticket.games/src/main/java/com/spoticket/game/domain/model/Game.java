package com.spoticket.game.domain.model;

import static io.micrometer.common.util.StringUtils.isNotBlank;
import static java.util.Objects.nonNull;

import com.spoticket.game.global.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
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
@Table(name = "p_games")
@Builder
public class Game extends BaseEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID gameId;

  private String title;

  private LocalDateTime startTime;

  @Enumerated(value = EnumType.STRING)
  private Sport sport;

  private String league;

  private UUID stadiumId;

  private UUID homeTeamId;

  private UUID awayTeamId;

  public static Game of(String title, LocalDateTime startTime, Sport sport, String league,
      UUID stadiumId, UUID homeTeamId, UUID awayTeamId) {
    return Game.builder()
        .title(title)
        .startTime(startTime)
        .sport(sport)
        .league(league)
        .stadiumId(stadiumId)
        .homeTeamId(homeTeamId)
        .awayTeamId(awayTeamId)
        .build();
  }

  public void update(String title, LocalDateTime startTime, String league, Sport sport,
      UUID stadiumId, UUID homeTeamId, UUID awayTeamId) {
      if (isNotBlank(title)) {
          this.title = title;
      }
      if (nonNull(startTime)) {
          this.startTime = startTime;
      }
      if (isNotBlank(league)) {
          this.league = league;
      }
      if (nonNull(sport)) {
          this.sport = sport;
      }
      if (nonNull(stadiumId)) {
          this.stadiumId = stadiumId;
      }
      if (nonNull(homeTeamId)) {
          this.homeTeamId = homeTeamId;
      }
    if (nonNull(awayTeamId))
      this.awayTeamId = awayTeamId;
  }

}
