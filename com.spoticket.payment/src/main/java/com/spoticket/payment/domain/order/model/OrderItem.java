package com.spoticket.payment.domain.order.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "item_name" , length = 120, nullable = false)
    private String itemName;

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

    public void updateOrder(Order Order) {
        this.order = Order;
    }

    public static OrderItem createOrderItem(UUID ticketId, String itemName,int price, int quantity, String createdBy) {
        return OrderItem.builder()
            .ticketId(ticketId)
            .itemName(itemName)
            .price(price)
            .quantity(quantity)
            .createdBy(createdBy)
            .createdAt(LocalDateTime.now())
            .build();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem orderItem)) return false;
        return Objects.equals(getOrderItemId(), orderItem.getOrderItemId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderItemId());
    }
}