package com.spoticket.teamstadium.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity(name = "P_FAV_TEAMS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FavTeam {

  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(updatable = false, nullable = false)
  private UUID favId;

  @ManyToOne
  @JoinColumn(name = "team_id", nullable = false)
  private Team team;

  @Column(nullable = false)
  private UUID userId;

}
