package com.spoticket.payment.domain.payment.model;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    PENDING("결제 대기"),
    COMPLETED("결제 완료"),
    CANCELLED("결제 취소"),
    DONE("결제 승인 완료");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }


public static PaymentStatus fromTossStatus(String tossStatus) {
    return switch (tossStatus) {
        case "DONE" -> COMPLETED;
        case "CANCELED" -> CANCELLED;
        default -> throw new IllegalArgumentException("알 수 없는 상태 값 : " + tossStatus);
    };
}
}
