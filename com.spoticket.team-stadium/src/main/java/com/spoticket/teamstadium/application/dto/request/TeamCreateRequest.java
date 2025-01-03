package com.spoticket.teamstadium.application.dto.request;

import com.spoticket.teamstadium.domain.model.TeamCategoryEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TeamCreateRequest(

    @NotNull @Size(max = 100) String name,
    @NotNull TeamCategoryEnum category,
    String description,
    @Size(max = 500) String profile,
    @Size(max = 300) String homeLink,
    @Size(max = 300) String snsLink

) {

}
