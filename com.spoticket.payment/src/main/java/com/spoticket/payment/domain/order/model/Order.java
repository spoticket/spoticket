package com.spoticket.payment.domain.order.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Builder.Default
    @OneToMany(cascade = PERSIST, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems = new ArrayList<>();


    public void addOrderItem(List<OrderItem> orderItems) {
        this.orderItems.addAll(orderItems);
        orderItems.forEach(orderItem -> orderItem.updateOrder(this));
    }

    public static Order createOrder(UUID userId, UUID userCouponId, List<OrderItem> orderItems) {
        long totalAmount = calculateTotalAmount(orderItems);

         Order order = Order.builder()
            .userId(userId)
            .userCouponId(userCouponId)
            .amount(totalAmount)
            .status(OrderStatus.CREATED)
            .createdBy(userId.toString())
            .createdAt(LocalDateTime.now())
            .updatedBy(userId.toString())
            .updatedAt(LocalDateTime.now())
            .build();

         order.addOrderItem(orderItems);
         return order;
    }

    private static long calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
            .mapToLong(item -> (long) item.getPrice() * item.getQuantity())
            .sum();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(getOrderId(), order.getOrderId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId());
    }
}