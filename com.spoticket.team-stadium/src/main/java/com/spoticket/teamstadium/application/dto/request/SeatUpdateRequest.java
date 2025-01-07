package com.spoticket.teamstadium.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SeatUpdateRequest(
    @NotBlank String section,
    @NotNull @Min(0) Long quantity,
    @NotNull @Min(0) Integer price
) {

}
