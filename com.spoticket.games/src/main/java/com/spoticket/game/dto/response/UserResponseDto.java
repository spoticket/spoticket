package com.spoticket.game.dto.response;

import java.sql.Date;
import java.util.UUID;

public record UserResponseDto(

    UUID userId,
    String email,
    String name,
    String gender,
    Date birthDate,
    Integer post,
    String address,
    String addressDetail,
    String slackId
    
) {

}