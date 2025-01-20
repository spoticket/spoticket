//package com.spoticket.payment;
//
//import com.spoticket.payment.domain.order.model.Order;
//import com.spoticket.payment.domain.order.model.OrderItem;
//import com.spoticket.payment.domain.order.model.OrderStatus;
//import com.spoticket.payment.domain.order.repository.OrderItemRepository;
//import com.spoticket.payment.domain.order.repository.OrderRepository;
//import java.time.LocalDateTime;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//import java.util.UUID;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@SpringBootTest
//class OrderTest {
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Test
//    @DisplayName("주문 생성 시 주문상품도 함께 생성되어야 한다")
//    void createOrderWithOrderItems() {
//        // given
//        UUID userId = UUID.randomUUID();
//        UUID userCouponId = UUID.randomUUID();
//        UUID ticketId = UUID.randomUUID();
//        LocalDateTime now = LocalDateTime.now();
//
//        List<OrderItem> orderItems = Stream.of(
//            OrderItem.builder()
//                .ticketId(ticketId)
//                .itemName("2024 방탄소년단 콘서트 티켓 R석")
//                .price(10000)
//                .quantity(2)
//                .createdBy(userId.toString())
//                .createdAt(now)
//                .build(),
//            OrderItem.builder()
//                .ticketId(ticketId)
//                .itemName("2024 방탄소년단 콘서트 티켓 S석")
//                .price(15000)
//                .quantity(1)
//                .createdBy(userId.toString())
//                .createdAt(now)
//                .build()
//        ).collect(Collectors.toList());
//
//        Order order = Order.createOrder(userId, userCouponId, orderItems);
//        orderRepository.save(order);
//
//        // then
//        Order foundOrder = orderRepository.findOrderWithItemsById(order.getOrderId())
//            .orElseThrow();
//
//        assertThat(foundOrder.getUserId()).isEqualTo(userId);
//        assertThat(foundOrder.getUserCouponId()).isEqualTo(userCouponId);
//        assertThat(foundOrder.getAmount()).isEqualTo(35000);
//        assertThat(foundOrder.getStatus()).isEqualTo(OrderStatus.CREATED);
//        assertThat(foundOrder.getCreatedBy()).isEqualTo(userId.toString());
//        assertThat(foundOrder.getUpdatedBy()).isEqualTo(userId.toString());
//
//        List<OrderItem> savedOrderItems = foundOrder.getOrderItems();
//        assertThat(savedOrderItems).hasSize(2);
//
//        OrderItem firstItem = savedOrderItems.get(0);
//        assertThat(firstItem.getItemName()).isEqualTo("2024 방탄소년단 콘서트 티켓 R석");
//        assertThat(firstItem.getPrice()).isEqualTo(10000);
//        assertThat(firstItem.getQuantity()).isEqualTo(2);
//        assertThat(firstItem.getCreatedBy()).isEqualTo(userId.toString());
//        assertThat(firstItem.getOrder()).isEqualTo(foundOrder);
//
//        OrderItem secondItem = savedOrderItems.get(1);
//        assertThat(secondItem.getItemName()).isEqualTo("2024 방탄소년단 콘서트 티켓 S석");
//        assertThat(secondItem.getPrice()).isEqualTo(15000);
//        assertThat(secondItem.getQuantity()).isEqualTo(1);
//        assertThat(secondItem.getCreatedBy()).isEqualTo(userId.toString());
//        assertThat(secondItem.getOrder()).isEqualTo(foundOrder);
//    }
//}