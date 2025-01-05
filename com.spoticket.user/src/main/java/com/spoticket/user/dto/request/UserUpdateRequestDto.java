package com.spoticket.user.dto.request;

import java.sql.Date;

public record UserUpdateRequestDto(
        String email,
        String password,
        String name,
        String gender,
        Date birthday,
        String slackId,
        Integer post,
        String address,
        String addressDetail
) {
}
