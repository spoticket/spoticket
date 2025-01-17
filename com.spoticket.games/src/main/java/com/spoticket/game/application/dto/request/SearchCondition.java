package com.spoticket.game.application.dto.request;

import com.spoticket.game.domain.model.Sport;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public record SearchCondition(
    String title,
    Sport sport,
    String league,
    UUID stadiumId,
    UUID teamId,
    LocalDateTime startDateTime,
    LocalDateTime endDateTime,
    Pageable pageable
) {

  public SearchCondition withPageable(Pageable pageable) {
    return new SearchCondition(
        title,
        sport,
        league,
        stadiumId,
        teamId,
        startDateTime,
        endDateTime,
        pageable
    );
  }

}
