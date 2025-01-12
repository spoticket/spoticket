package com.spoticket.payment.infrastrucutre.toss.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record TossPaymentFailureRequest(
    @JsonIgnore
    String paymentKey,
    String orderId,
    String message,
    String code
) {}