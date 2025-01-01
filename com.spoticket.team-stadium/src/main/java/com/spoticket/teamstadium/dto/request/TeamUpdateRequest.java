package com.spoticket.teamstadium.dto.request;

import jakarta.validation.constraints.NotNull;

public record TeamUpdateRequest(

    @NotNull String name,
    @NotNull String description,
    String profile,
    String homeLink,
    String snsLink
) {

}
