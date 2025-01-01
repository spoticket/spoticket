package com.spoticket.teamstadium.dto.request;

import com.spoticket.teamstadium.domain.TeamCategoryEnum;
import jakarta.validation.constraints.NotNull;

public record TeamCreateRequest(

    @NotNull String name,
    @NotNull TeamCategoryEnum category,
    @NotNull String description,
    String profile,
    String homeLink,
    String snsLink

) {

}
