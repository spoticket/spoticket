package com.spoticket.teamstadium.factory;

import com.spoticket.teamstadium.domain.model.Team;
import com.spoticket.teamstadium.domain.model.TeamCategoryEnum;
import java.util.UUID;

public class TeamTestFactory {

  public static Team createWithId(
      UUID teamId,
      String name,
      TeamCategoryEnum category,
      String description,
      String profile,
      String homeLink,
      String snsLink
  ) {
    return Team.builder()
        .teamId(teamId)
        .name(name)
        .category(category)
        .description(description)
        .profile(profile)
        .homeLink(homeLink)
        .snsLink(snsLink)
        .build();
  }
}
