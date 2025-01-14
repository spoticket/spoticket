package com.spoticket.payment.application.order.dto;

import com.spoticket.payment.domain.order.model.OrderItem;
import java.util.List;
import java.util.UUID;
import lombok.Getter;

@Getter
public class CreateOrderReq {

    private UUID userID;
    private UUID userCouponId;
    private List<OrderItemReq> items;

    @Getter
    public static class OrderItemReq {
        private UUID ticketId;
        private int price;
        private String itemName;
    }

}
