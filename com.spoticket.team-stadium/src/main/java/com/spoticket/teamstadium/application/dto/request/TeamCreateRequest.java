package com.spoticket.teamstadium.application.dto.request;

import com.spoticket.teamstadium.domain.model.TeamCategoryEnum;
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
