package com.spoticket.payment.application.order.dto;

import com.spoticket.payment.domain.order.model.Order;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OrderRes {


    private UUID orderId;
    private long totalAmount;
    private List<OrderItemRes> orderItemRes;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class OrderItemRes {
        private String itemName;
    }

    public static OrderRes from(Order order) {
        return OrderRes.builder()
            .orderId(order.getOrderId())
            .totalAmount(order.getAmount())
            .orderItemRes(order.getOrderItems().stream()
                .map(item -> OrderItemRes.builder()
                    .itemName(item.getItemName())
                    .build())
                .collect(Collectors.toList()))
            .build();
    }
}
