package com.spoticket.payment.domain.order.repository;

import com.spoticket.payment.domain.order.model.OrderItem;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {

}
