package com.spoticket.user.dto.response;

import java.util.UUID;

public record UserCouponResponseDto(
        UUID userCouponId,
        UUID userId,
        UUID couponId,
        String status
) {
}
