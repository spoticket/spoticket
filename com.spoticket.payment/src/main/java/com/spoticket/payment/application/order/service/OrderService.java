package com.spoticket.payment.application.order.service;


import com.spoticket.payment.application.order.dto.CreateOrderReq;
import com.spoticket.payment.application.order.dto.OrderRes;
import com.spoticket.payment.domain.order.model.Order;
import com.spoticket.payment.domain.order.model.OrderItem;
import com.spoticket.payment.domain.order.repository.OrderItemRepository;
import com.spoticket.payment.domain.order.repository.OrderRepository;
import com.spoticket.payment.domain.order.service.OrderDomainService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderDomainService orderDomainService;


    @Transactional
    public OrderRes createOrder(CreateOrderReq createOrderReq) {
        if (createOrderReq.getUserCouponId() != null) {
            validateHasCoupon(createOrderReq.getUserCouponId());
        }
        List<OrderItem> orderItems = createOrderItems(createOrderReq);

        Order order = Order.createOrder(createOrderReq.getUserID(),
            createOrderReq.getUserCouponId(),
            createOrderReq.getUserID(),
            orderItems);

        order.calculateAndSetTotalAmount(orderDomainService);

        return OrderRes.from(orderRepository.save(order));
    }

    private List<OrderItem> createOrderItems(CreateOrderReq createOrderReq) {
        return createOrderReq.getItems().stream()
            .map(item -> OrderItem.builder()
                .itemName(item.getItemName())
                .ticketId(item.getTicketId())
                .price(item.getPrice())
                .createdAt(LocalDateTime.now())
                .build())
            .collect(Collectors.toList());
    }

    public void validateHasCoupon(UUID couponId) {

    }
}
