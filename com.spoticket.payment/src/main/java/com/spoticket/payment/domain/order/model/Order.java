package com.spoticket.payment.domain.order.model;

import com.spoticket.payment.domain.order.service.OrderDomainService;
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
    private UUID createdBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 50)
    private UUID updatedBy;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(length = 50)
    private UUID deletedBy;

    private LocalDateTime deletedAt;

    @OneToMany(cascade = PERSIST, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> orderItems;

    public static Order createOrder(UUID userId, UUID userCouponId , UUID createdBy, List<OrderItem> orderItems) {
        Order order = Order.builder()
            .userId(userId)
            .userCouponId(userCouponId)
            .status(OrderStatus.CREATED)
            .createdAt(LocalDateTime.now())
            .createdBy(userId)
            .updatedBy(userId)
            .updatedAt(LocalDateTime.now())
            .build();

        order.addOrderItem(orderItems);

        return order;
    }
    public void calculateAndSetTotalAmount(OrderDomainService orderDomainService) {
        this.amount = orderDomainService.calculateTotalPrice(this.orderItems);
    }
    public void calculatePriceWithCouponDiscount(OrderDomainService orderDomainService, Double discountRate) {
        this.amount = orderDomainService.calculatePriceWithCouponDiscount(this.amount, discountRate);
    }

    public void addOrderItem(List<OrderItem> orderItem) {

        this.orderItems.addAll(orderItem);
        orderItem.forEach(orderItems -> orderItems.updateOrder(this));
    }
}