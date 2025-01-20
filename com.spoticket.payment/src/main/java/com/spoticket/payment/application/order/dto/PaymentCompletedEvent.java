package com.spoticket.payment.application.order.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCompletedEvent {
    private UUID orderId;
    private String status;

}