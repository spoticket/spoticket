package com.spoticket.game.dto.request;

import com.spoticket.game.domain.model.Sport;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreateLeagueRequest(
    @NotBlank @Size(max = 200) String name,
    @Min(0) int season,
    @NotNull LocalDate startAt,
    @NotNull LocalDate endAt,
    @NotNull Sport sport
) {

}
