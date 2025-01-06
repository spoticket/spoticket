package com.spoticket.payment.domain.order.repository;

import com.spoticket.payment.domain.order.model.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}
