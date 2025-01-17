package com.spoticket.game.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

public record SuccessResponse<T>(
    Integer code,
    String message,
    @JsonInclude(JsonInclude.Include.NON_NULL) T data
) {

}
