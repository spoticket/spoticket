package com.spoticket.teamstadium.application.dto.request;

import jakarta.validation.constraints.NotNull;

public record StadiumCreateRequest(

    @NotNull String name,
    @NotNull String address,
    @NotNull double lat,
    @NotNull double lng,
    String seatImage,
    String description
) {

}
