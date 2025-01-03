package com.spoticket.teamstadium.application.dto.response;

import com.spoticket.teamstadium.domain.model.TeamCategoryEnum;
import java.time.LocalDateTime;
import java.util.UUID;

public record GameReadResponse(
    UUID gameId,
    String title,
    LocalDateTime startTime,
    TeamCategoryEnum sport,
    String league

) {

}
