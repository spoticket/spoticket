package com.spoticket.payment.domain.order.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.CascadeType.PERSIST;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "P_ORDERS", schema = "payment_service")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    @Column(nullable = false)
    private UUID userId;

    private UUID userCouponId;

    @Column(nullable = false)
    private long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(nullable = false, length = 50)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 50)
    private String updatedBy;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(length = 50)
    private String deletedBy;

    private LocalDateTime deletedAt;

    @OneToMany(cascade = PERSIST, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems;
}