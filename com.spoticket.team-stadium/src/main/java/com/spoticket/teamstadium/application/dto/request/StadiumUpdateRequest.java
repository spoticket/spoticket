package com.spoticket.teamstadium.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StadiumUpdateRequest(
    @NotBlank @Size(max = 100) String name,
    @NotBlank @Size(max = 200) String address,
    @Size(max = 500) String seatImage,
    String description,
    @NotNull double lat,
    @NotNull double lng
) {

}
