package com.spoticket.payment.infrastrucutre.toss.dto;

import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record TossPaymentRes(String paymentKey, String orderId, String orderName, String status,
                             Long totalAmount, OffsetDateTime approvedAt, String method, String description, Card card) {

    public record Card(String issuerCode, int installmentPlanMonths) {

    }
}
