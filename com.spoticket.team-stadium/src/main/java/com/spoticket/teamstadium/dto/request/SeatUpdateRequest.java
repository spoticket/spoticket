package com.spoticket.teamstadium.dto.request;

import jakarta.validation.constraints.NotNull;

public record SeatUpdateRequest(
    @NotNull String section,
    @NotNull Long quantity,
    @NotNull Integer price
) {

}
