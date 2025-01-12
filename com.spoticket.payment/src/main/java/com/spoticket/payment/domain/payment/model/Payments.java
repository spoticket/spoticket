package com.spoticket.payment.domain.payment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "p_payments", schema = "payment_service")
public class Payments {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "amount", nullable = false)
    private long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private PaymentStatus status;

    @Column(name = "payment_key", length = 201)
    private String paymentKey;

    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by", length = 50, nullable = false)
    private String updatedBy;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static Payments createPayments(UUID orderId, long amount) {
        return Payments.builder()
            .orderId(orderId)
            .amount(amount)
            .status(PaymentStatus.PENDING)
            .createdBy(orderId.toString())
            .createdAt(LocalDateTime.now())
            .updatedBy(orderId.toString())
            .updatedAt(LocalDateTime.now())
            .build();
    }
    public void updateToComplete(String paymentKey) {
        this.paymentKey = paymentKey;
        this.status = PaymentStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateToCancel() {
        this.status = PaymentStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

}
