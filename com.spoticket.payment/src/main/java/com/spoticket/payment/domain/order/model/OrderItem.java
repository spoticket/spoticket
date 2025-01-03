package com.spoticket.payment.domain.order.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "P_ORDER_ITEMS", schema = "payment_service")
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_item_id")
    private UUID orderItemId;

    @Column(nullable = false)
    private UUID ticketId;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, length = 20)
    private int quantity;

    @Column(nullable = false, length = 50)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}