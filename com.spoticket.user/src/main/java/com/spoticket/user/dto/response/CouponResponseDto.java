package com.spoticket.user.dto.response;

import java.sql.Date;
import java.util.UUID;

public record CouponResponseDto(
        UUID couponId,
        String name,
        Date expirationAt,
        Boolean isAble,
        String type,
        Double discountRate,
        Long stock,
        Boolean isActive
) {
}
