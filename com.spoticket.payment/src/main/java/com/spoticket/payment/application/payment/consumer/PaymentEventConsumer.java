package com.spoticket.payment.application.payment.consumer;

import com.spoticket.payment.application.order.dto.PaymentRequestEvent;
import com.spoticket.payment.domain.payment.exception.PaymentException;
import com.spoticket.payment.domain.payment.model.PaymentConsumer;
import com.spoticket.payment.domain.payment.repository.PaymentConsumerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {


    private final PaymentConsumerRepository paymentConsumerRepository;

    @KafkaListener(
        topics = "${spring.kafka.topic.payment-request}",
        groupId = "${spring.kafka.consumer.group-id}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumePaymentRequest(
        @Payload PaymentRequestEvent event,
        @Header(KafkaHeaders.RECEIVED_KEY) String key,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset
    ) {
        log.info("받은 요청 - orderId: {}, itemName: {}, amount: {}, partition: {}, offset: {}",
            event.getOrderId(), event.getItemName(), event.getAmount(), partition, offset);
        try {
            saveTopic(event);
            log.info("Successfully processed payment request for orderId: {}", event.getOrderId());
        } catch (PaymentException e) {
            log.error("Payment processing failed for orderId: {} - {}", event.getOrderId(), e.getMessage());
            throw e; // 예외 로직 작성해야함
        } catch (Exception e) {
            log.error("Unexpected error while processing payment request for orderId: {}", event.getOrderId(), e);
            throw e;
        }
    }

    @Transactional
    public void saveTopic(PaymentRequestEvent event) {
        PaymentConsumer consumer = PaymentConsumer.toEntity(
            event.getOrderId(), event.getItemName(), event.getAmount()
        );
        paymentConsumerRepository.save(consumer);
    }

}