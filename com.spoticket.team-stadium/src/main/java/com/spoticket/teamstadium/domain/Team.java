package com.spoticket.teamstadium.domain;

import com.spoticket.teamstadium.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity(name = "P_TEAMS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Team extends BaseEntity {

  @Id
  @GeneratedValue
  @UuidGenerator
  @Column(updatable = false, nullable = false)
  private UUID teamId;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private TeamCategoryEnum category;

  private String description;

  private String profile;

  private String homeLink;

  private String snsLink;

  public static Team create(
      String name,
      TeamCategoryEnum category,
      String description,
      String profile,
      String homeLink,
      String snsLink
  ) {
    return Team.builder()
        .name(name)
        .category(category)
        .description(description)
        .profile(profile)
        .homeLink(homeLink)
        .snsLink(snsLink)
        .build();
  }

  public void update(
      String name,
      String description,
      String profile,
      String homeLink,
      String snsLink
  ) {
    if (name != null) {
      this.name = name;
    }
    if (description != null) {
      this.description = description;
    }
    if (profile != null) {
      this.profile = profile;
    }
    if (homeLink != null) {
      this.homeLink = homeLink;
    }
    if (snsLink != null) {
      this.snsLink = snsLink;
    }
  }
}
