package com.spoticket.payment.application.order.dto;

import java.util.List;
import java.util.UUID;
import lombok.Getter;

public record OrderCreateReq(
    UUID userId,
    UUID userCouponId,
    List<OrderItemReq> items
) {
    public record OrderItemReq(
        UUID ticketId,
        String itemName,
        int price,
        int quantity
    ) {}
}
