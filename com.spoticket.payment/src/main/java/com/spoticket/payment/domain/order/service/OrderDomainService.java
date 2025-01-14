package com.spoticket.payment.domain.order.service;

import com.spoticket.payment.application.order.dto.CreateOrderReq.OrderItemReq;
import com.spoticket.payment.domain.order.model.OrderItem;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderDomainService {


    public long calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
            .mapToLong(OrderItem::getPrice)
            .sum();
    }

}
