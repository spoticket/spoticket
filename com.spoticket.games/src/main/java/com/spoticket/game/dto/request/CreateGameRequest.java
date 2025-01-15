package com.spoticket.game.dto.request;

import com.spoticket.game.domain.model.Sport;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CreateGameRequest {

  @NotBlank
  private String title;

  @NotNull
  @Future
  private LocalDateTime startTime;

  @NotNull
  private Sport sport;
  
  private UUID leagueId;

  @NotNull
  private UUID stadiumId;

  @NotNull
  private UUID homeTeamId;

  @NotNull
  private UUID awayTeamId;

}
