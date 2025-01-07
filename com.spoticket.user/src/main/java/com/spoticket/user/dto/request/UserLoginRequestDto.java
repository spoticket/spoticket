package com.spoticket.user.dto.request;

public record UserLoginRequestDto(
        String email,
        String password
) {
}
