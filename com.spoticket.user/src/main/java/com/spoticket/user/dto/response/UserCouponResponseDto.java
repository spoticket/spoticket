package com.spoticket.user.dto.response;

import java.sql.Date;
import java.util.UUID;

public record UserCouponResponseDto(
        UUID userCouponId,
        UUID userId,
        UUID couponId,
        String status,
        String name,
        Date expirationAt,
        String type,
        Double discountRate,
        Boolean isActive
) {
}
