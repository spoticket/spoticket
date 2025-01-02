package com.spoticket.teamstadium.application.dto.response;

import com.spoticket.teamstadium.domain.model.Team;
import com.spoticket.teamstadium.domain.model.TeamCategoryEnum;
import java.util.UUID;

public record TeamInfoResponse(
    UUID teamId,
    String name,
    TeamCategoryEnum category,
    String description,
    String profile,
    String homeLink,
    String snsLink,
    Integer favCnt,
    Boolean isFavorite
) {

  public static TeamInfoResponse from(Team team, Integer favCnt, boolean isFav) {
    return new TeamInfoResponse(
        team.getTeamId(),
        team.getName(),
        team.getCategory(),
        team.getDescription(),
        team.getProfile(),
        team.getHomeLink(),
        team.getSnsLink(),
        favCnt,
        isFav
    );
  }
}
