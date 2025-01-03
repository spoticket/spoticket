package com.spoticket.teamstadium.application.dto.response;

import com.spoticket.teamstadium.domain.model.Team;
import com.spoticket.teamstadium.domain.model.TeamCategoryEnum;
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
