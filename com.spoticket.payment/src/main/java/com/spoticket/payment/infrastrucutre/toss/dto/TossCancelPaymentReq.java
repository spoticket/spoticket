package com.spoticket.payment.infrastrucutre.toss.dto;

public record TossCancelPaymentReq(
   String paymentKey,
   String cancelReason
) {}
