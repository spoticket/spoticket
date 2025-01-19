package com.spoticket.payment.application.order.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestEvent {
    private UUID orderId;
    private String itemName;
    private Long amount;


}