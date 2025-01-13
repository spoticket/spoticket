package com.spoticket.payment.application.order.service;


import com.spoticket.payment.application.order.dto.OrderCreateReq;
import com.spoticket.payment.application.order.dto.OrderRes;
import com.spoticket.payment.application.order.dto.OrderRes.OrderItemRes;
import com.spoticket.payment.application.order.event.OrderCreatedEvent;
import com.spoticket.payment.application.payment.dto.PaymentCreateReq;
import com.spoticket.payment.domain.order.exception.OrderErrorCode;
import com.spoticket.payment.domain.order.exception.OrderException;
import com.spoticket.payment.domain.order.model.Order;
import com.spoticket.payment.domain.order.model.OrderItem;
import com.spoticket.payment.domain.order.repository.OrderItemRepository;
import com.spoticket.payment.domain.order.repository.OrderRepository;
//import com.spoticket.payment.infrastrucutre.feign.client.PaymentClient;
//import com.spoticket.payment.presentation.common.ApiSuccessResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final String ORDER_CREATED_TOPIC = "order.created";
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public OrderRes createOrder(OrderCreateReq orderCreateReq) {
        List<OrderItem> orderItems = createOrderItems(orderCreateReq);
        
        Order order = Order.createOrder(
            orderCreateReq.userId(),
            orderCreateReq.userCouponId(),
            orderItems
        );
        Order savedOrder = orderRepository.save(order);
        OrderCreatedEvent event = OrderCreatedEvent.from(savedOrder);
        kafkaTemplate.send(ORDER_CREATED_TOPIC, event);
        return OrderRes.from(savedOrder);
    }

    private List<OrderItem> createOrderItems(OrderCreateReq orderCreateReq) {
        return orderCreateReq.items().stream()
            .map(item -> OrderItem.createOrderItem(
                item.ticketId(),
                item.itemName(),
                item.price(),
                item.quantity(),
                orderCreateReq.userId().toString()
            )).collect(Collectors.toList());
    }

    public OrderRes getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException(
            OrderErrorCode.ORDER_NOT_FOUND));
        return OrderRes.from(order);
    }

}