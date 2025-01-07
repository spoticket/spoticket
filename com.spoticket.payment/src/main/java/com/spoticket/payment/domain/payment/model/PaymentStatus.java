package com.spoticket.payment.domain.payment.model;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING("결제 대기"),
    CANCEL("결제 취소"),
    COMPLETED("결제 완료");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }
}
