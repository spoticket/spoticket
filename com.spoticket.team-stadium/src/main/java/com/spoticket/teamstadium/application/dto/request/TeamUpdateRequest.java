package com.spoticket.teamstadium.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeamUpdateRequest(
    @NotBlank @Size(max = 100) String name,
    String description,
    @Size(max = 500) String profile,
    @Size(max = 300) String homeLink,
    @Size(max = 300) String snsLink
) {

}
