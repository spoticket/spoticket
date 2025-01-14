package com.spoticket.payment.application.payment.dto;

import com.spoticket.payment.domain.order.model.OrderItem;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class PaymentCreateReq {

    private UUID orderId;
    private long amount;
    private String itemName;
}
