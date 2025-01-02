package com.spoticket.user.dto.request;

import java.sql.Date;

public record CouponCreateRequestDto(
        String name,
        Date expirationAt,
        Boolean isAble,
        String type,
        Double discountRate,
        Long stock,
        Boolean isActive
) {
}
