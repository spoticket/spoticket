package com.spoticket.teamstadium.dto.response;

import com.spoticket.teamstadium.domain.TeamCategoryEnum;
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
