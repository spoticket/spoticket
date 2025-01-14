package com.spoticket.teamstadium.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record SeatCreateRequest(
    @NotNull UUID gameId,
    @NotBlank @Size(max = 100) String section,
    @NotNull @Min(0) Long quantity,
    @NotNull @Min(0) Integer price
) {

}
