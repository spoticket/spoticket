package com.spoticket.payment.infrastrucutre.toss.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record TossPaymentRes(String paymentKey, UUID orderId, String orderName, String status,
                             Long totalAmount, OffsetDateTime approvedAt, String method, String description, @JsonIgnore Card card) {

    public record Card(String issuerCode, int installmentPlanMonths) {

    }
}
