package com.spoticket.payment.infrastrucutre.toss.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record TossPaymentReq(
    String paymentKey,
    UUID orderId,
    Long amount) {

}