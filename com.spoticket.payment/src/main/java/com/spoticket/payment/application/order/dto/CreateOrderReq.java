package com.spoticket.payment.application.order.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateOrderReq {

    @JsonProperty("userId")
    private UUID userId;
    private UUID userCouponId;
    private List<OrderItemReq> items = new ArrayList<>();

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemReq {
        private UUID ticketId;
        private int price;
        private String itemName;
    }

}
