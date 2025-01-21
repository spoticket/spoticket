package com.spoticket.payment.infrastrucutre.order.feign.dto;

import java.util.Date;
import java.util.UUID;
import lombok.Getter;


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