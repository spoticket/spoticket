package com.spoticket.ticket.application.dtos.response;

import java.util.UUID;

public record UserResponseDto(
    UUID userId,
    String name
) {

}
