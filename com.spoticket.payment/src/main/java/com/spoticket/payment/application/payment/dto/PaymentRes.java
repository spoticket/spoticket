package com.spoticket.payment.application.payment.dto;

import com.spoticket.payment.domain.payment.model.PaymentStatus;
import com.spoticket.payment.domain.payment.model.Payments;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentRes (UUID paymentId, UUID orderId, long amount, PaymentStatus status, LocalDateTime created_at ){

    public static PaymentRes from(Payments payments) {
        return new PaymentRes(payments.getPaymentId(),
            payments.getOrderId(),
            payments.getAmount(),
            payments.getStatus(),
            payments.getCreatedAt());
    }
}
