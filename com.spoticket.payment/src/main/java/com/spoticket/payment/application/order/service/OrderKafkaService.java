package com.spoticket.payment.application.order.service;

import com.spoticket.payment.application.order.dto.PaymentRequestEvent;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderKafkaService {

    private final KafkaTemplate<String, PaymentRequestEvent> kafkaTemplate;


    @Value("${spring.kafka.topic.payment-request}")
    private String paymentRequestTopic;

    public void sendPaymentRequest(PaymentRequestEvent event) {
        try {

            CompletableFuture<SendResult<String, PaymentRequestEvent>> future =
                kafkaTemplate.send(paymentRequestTopic, event.getOrderId().toString(), event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("결제 생성 전송 성공 - orderId: {}, partition: {}, offset: {}",
                        event.getOrderId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                } else {
                    log.error("결제 생성 전송 실패 - orderId: {}", event.getOrderId(), ex);
                }
            });
        } catch (Exception e) {
            log.error( "결제 요청을 보내는 중 오류가 발생했습니다 - orderId: {}", event.getOrderId(),
                e);
            throw e;
        }
            log.info("결제 요청 이벤트가 성공적으로 전송되었습니다. orderId: {}", event.getOrderId());
        }
    }
