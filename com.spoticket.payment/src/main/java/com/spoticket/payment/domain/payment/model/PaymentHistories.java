package com.spoticket.payment.domain.payment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "P_payment_histories", schema = "payment_service")
public class PaymentHistories {

    @Id  @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "histories_id")
    private UUID historiesId;

    @Column(name = "payment_id", nullable = false)
    private UUID paymentId;

    @Column(nullable = false)
    private long amount;

    @Column(name = "issuer_code", length = 10)
    private String issuerCode;

    @Column(name = "installment_months")
    private int installmentMonths;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private PaymentStatus status;


    @Column(name = "payment_method", length = 20, nullable = false)
    private String paymentMethod;

    @Column(name = "description")
    private String description;

    @Column(name = "created_by", length = 50, nullable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by", length = 50, nullable = false)
    private String updatedBy;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public static PaymentHistories createPaymentStatusHistory(Payments payment, String method ,String tossStatus, String description , String issuerCode,         // 새로운 파라미터
        int installmentMonths  ) {
        return PaymentHistories.builder()
            .paymentId(payment.getPaymentId())
            .status(PaymentStatus.fromTossStatus(tossStatus))
            .issuerCode(issuerCode)
            .installmentMonths(installmentMonths)
            .amount(payment.getAmount())
            .paymentMethod(method)
            .description(description)
            .createdBy(payment.getOrderId().toString())
            .createdAt(LocalDateTime.now())
            .updatedBy(payment.getOrderId().toString())
            .updatedAt(LocalDateTime.now())
            .build();
    }

}
