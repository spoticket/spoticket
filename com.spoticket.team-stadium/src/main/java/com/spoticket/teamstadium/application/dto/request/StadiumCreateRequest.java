package com.spoticket.teamstadium.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StadiumCreateRequest(

    @NotNull @Size(max = 100) String name,
    @NotNull @Size(max = 200) String address,
    @NotNull double lat,
    @NotNull double lng,
    @Size(max = 500) String seatImage,
    String description
) {

}
