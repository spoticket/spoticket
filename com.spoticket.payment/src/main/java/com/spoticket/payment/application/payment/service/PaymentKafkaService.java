package com.spoticket.payment.application.payment.service;

import com.spoticket.payment.application.order.dto.PaymentCompletedEvent;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentKafkaService {
    
    private final KafkaTemplate<String, PaymentCompletedEvent> paymentCompletedKafkaTemplate;
    
    @Value("${spring.kafka.topic.payment-completed}")
    private String paymentCompletedTopic;

    public void sendPaymentCompleted(UUID orderId, String status) {
        PaymentCompletedEvent event = new PaymentCompletedEvent(orderId, status);
        log.info("Sending payment completed event - orderId: {}, status: {}, event: {}",
            orderId, status, event);
        try {
            CompletableFuture<SendResult<String, PaymentCompletedEvent>> future =
                paymentCompletedKafkaTemplate.send(
                    paymentCompletedTopic,
                    orderId.toString(),
                    event
                );

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("결제 완료 이벤트 전송 성공 - orderId: {}, partition: {}, offset: {}",
                        orderId,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                } else {
                    log.error("결제 완료 이벤트 전송 실패 - orderId: {}", orderId, ex);
                }
            });
        } catch (Exception e) {
            log.error("결제 완료 이벤트 전송 중 오류 발생 - orderId: {}", orderId, e);
            throw e;
        }
    }
}