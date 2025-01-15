package com.spoticket.game.domain.model;

import com.spoticket.game.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity(name = "p_leagues")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class League extends BaseEntity {

  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(updatable = false, nullable = false)
  private UUID leagueId;

  @Column(nullable = false, length = 200)
  private String name;

  @Column(nullable = false)
  private Sport sport;

  @Column(nullable = false)
  private int season;

  private LocalDate startAt;

  private LocalDate endAt;

  @OneToMany(mappedBy = "league")
  private List<LeagueGame> leagueGames = new ArrayList<>();

  @OneToMany(mappedBy = "league")
  private List<LeagueTeam> leagueTeams = new ArrayList<>();

  @OneToMany(mappedBy = "league")
  private List<Game> games = new ArrayList<>();

  public static League create(
      String name,
      Sport sport,
      int season,
      LocalDate startAt,
      LocalDate endAt
  ) {
    return League.builder()
        .name(name)
        .sport(sport)
        .season(season)
        .startAt(startAt)
        .endAt(endAt)
        .build();
  }
}
