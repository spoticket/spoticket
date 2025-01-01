package com.spoticket.teamstadium.dto.response;

import com.spoticket.teamstadium.domain.Team;
import com.spoticket.teamstadium.domain.TeamCategoryEnum;
import java.util.UUID;

public record TeamListReadResponse(

    UUID teamId,
    String name,
    TeamCategoryEnum category
) {

  public static TeamListReadResponse from(Team team) {
    return new TeamListReadResponse(
        team.getTeamId(),
        team.getName(),
        team.getCategory()
    );
  }

}
