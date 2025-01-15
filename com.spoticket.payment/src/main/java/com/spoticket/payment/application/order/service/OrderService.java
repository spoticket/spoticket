package com.spoticket.payment.application.order.service;


import com.spoticket.payment.application.order.dto.CreateOrderReq;
import com.spoticket.payment.application.order.dto.OrderRes;
import com.spoticket.payment.domain.order.exception.OrderErrorCode;
import com.spoticket.payment.domain.order.exception.OrderException;
import com.spoticket.payment.domain.order.model.Order;
import com.spoticket.payment.domain.order.model.OrderItem;
import com.spoticket.payment.domain.order.repository.OrderRepository;
import com.spoticket.payment.domain.order.service.OrderDomainService;
import com.spoticket.payment.infrastrucutre.order.feign.dto.UserCouponResponseDto;
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
    private final OrderDomainService orderDomainService;

    @Transactional
    public OrderRes createOrder(CreateOrderReq createOrderReq) {

        List<OrderItem> orderItems = createOrderItems(createOrderReq);

        Order order = Order.createOrder(createOrderReq.getUserID(),
            createOrderReq.getUserCouponId(),
            createOrderReq.getUserID(),
            orderItems);

         order.calculateAndSetTotalAmount(orderDomainService);

         //쿠폰 객체가 파라미터로 들어오면 쿠폰 검증 시작
        if (createOrderReq.getUserCouponId() != null) {
            UserCouponResponseDto couponInfo = orderDomainService.validateUserCoupon(
                createOrderReq.getUserCouponId(),
                createOrderReq.getUserID()
            );
            // 검증이 완료되면 쿠폰적용가 계산을 위한 메소드를 호출
            order.calculatePriceWithCouponDiscount(orderDomainService, couponInfo.discountRate());
        }
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

    public OrderRes getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new OrderException(
            OrderErrorCode.ORDER_NOT_FOUND));
        return OrderRes.from(order);
    }

}
