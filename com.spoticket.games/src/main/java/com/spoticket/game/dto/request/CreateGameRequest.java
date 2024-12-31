package com.spoticket.game.dto.request;

import com.spoticket.game.domain.model.Sport;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class CreateGameRequest {

    @NotBlank
    private String title;

    @NotNull
    @Future
    private LocalDateTime startTime;

    @NotNull
    private Sport sport;

    @NotBlank
    private String league;

    @NotNull
    private UUID stadiumId;

    @NotNull
    private UUID homeTeamId;

    @NotNull
    private UUID awayTeamId;

}
