package com.spoticket.game.dto.request;

import com.spoticket.game.domain.model.Sport;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UpdateGameRequest {

  private String title;

  @Future
  private LocalDateTime startTime;

  private Sport sport;

  private String league;

  private UUID stadiumId;

  private UUID homeTeamId;

  private UUID awayTeamId;

}
