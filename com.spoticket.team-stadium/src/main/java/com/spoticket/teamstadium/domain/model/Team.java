package com.spoticket.teamstadium.domain.model;

import com.spoticket.teamstadium.global.common.BaseEntity;
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
import org.springframework.util.StringUtils;

@Entity(name = "p_teams")
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

  @Column(nullable = false, length = 100)
  private String name;

  @Column(nullable = false, length = 20)
  private TeamCategoryEnum category;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(length = 500)
  private String profile;

  @Column(length = 300)
  private String homeLink;

  @Column(length = 300)
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
    if (name != null && StringUtils.hasText(name) && name.length() <= 100) {
      this.name = name;
    }
    this.description = description;
    if (profile == null || profile.length() <= 500) {
      this.profile = profile;
    }
    if (homeLink == null || homeLink.length() <= 300) {
      this.homeLink = homeLink;
    }
    if (snsLink == null || snsLink.length() <= 300) {
      this.snsLink = snsLink;
    }
  }
}
