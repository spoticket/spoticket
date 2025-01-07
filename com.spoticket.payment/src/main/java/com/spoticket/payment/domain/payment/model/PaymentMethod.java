package com.spoticket.payment.domain.payment.model;

public enum PaymentMethod {
    CARD("카드"),
    TRANSFER("계좌이체");

    PaymentMethod(String description) {
    }
}
