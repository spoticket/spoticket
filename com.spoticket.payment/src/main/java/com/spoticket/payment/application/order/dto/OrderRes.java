package com.spoticket.payment.application.order.dto;

import com.spoticket.payment.domain.order.model.Order;
import com.spoticket.payment.domain.order.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderRes(
    UUID orderId,
    UUID userId,
    UUID userCouponId,
    long totalAmount,
    OrderStatus status,
    List<OrderItemRes> items,
    LocalDateTime createdAt
) {
    public record OrderItemRes(
        UUID orderItemId,
        UUID ticketId,
        String itemName,
        int price,
        int quantity
    ) {}
    public static OrderRes from(Order order) {
        List<OrderItemRes> itemResponses = order.getOrderItems().stream()
            .map(item -> new OrderItemRes(
                item.getOrderItemId(),
                item.getTicketId(),
                item.getItemName(),
                item.getPrice(),
                item.getQuantity()
            ))
            .toList();

        return new OrderRes(
            order.getOrderId(),
            order.getUserId(),
            order.getUserCouponId(),
            order.getAmount(),
            order.getStatus(),
            itemResponses,
            order.getCreatedAt()
        );
    }
}