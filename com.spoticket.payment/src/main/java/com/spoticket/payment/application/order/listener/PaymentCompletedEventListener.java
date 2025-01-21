package com.spoticket.payment.application.order.listener;

import com.spoticket.payment.application.order.dto.PaymentCompletedEvent;
import com.spoticket.payment.application.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentCompletedEventListener {

    private final OrderService orderService;

    @Transactional
    @KafkaListener(
        topics = "${spring.kafka.topic.payment-completed}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "paymentCompletedListenerContainerFactory"
    )
    public void handlePaymentCompleted(
        @Payload PaymentCompletedEvent event,
        Acknowledgment acknowledgment
    ) {
        try {
            log.info("결제 완료 이벤트 수신 - orderId: {}", event.getOrderId());
            if (event.getStatus().equals("DONE")) {
                orderService.completeOrder(event.getOrderId());
            } else {
                orderService.canceledOrder(event.getOrderId());
            }

            log.info("주문 상태 업데이트 완료 - orderId: {}", event.getOrderId());
            acknowledgment.acknowledge();
            
        } catch (Exception e) {
            log.error("주문 상태 업데이트 실패 - orderId: {}", event.getOrderId(), e);
            throw e;
        }
    }
}