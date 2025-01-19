package com.spoticket.payment.application.payment.dto;

import com.spoticket.payment.domain.payment.model.PaymentConsumer;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PaymentEventRes {

    private UUID orderId;
    private long amount;
    private String itemName;

    public static PaymentEventRes from(PaymentConsumer paymentConsumer) {
        return new PaymentEventRes(paymentConsumer.getOrderId(), paymentConsumer.getAmount(), paymentConsumer.getItemName());
    }
}
