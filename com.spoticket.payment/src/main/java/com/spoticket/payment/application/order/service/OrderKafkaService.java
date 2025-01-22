package com.spoticket.payment.application.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spoticket.payment.application.order.dto.PaymentRequestEvent;
import java.util.List;
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
public class OrderKafkaService {

    private final KafkaTemplate<String, PaymentRequestEvent> kafkaTemplate;
    private final KafkaTemplate<String, String> stringKafkaTemplate;


    @Value("${spring.kafka.topic.payment-request}")
    private String paymentRequestTopic;
    @Value("${spring.kafka.topic.coupon-used}")
    private String couponUsedTopic;
    @Value("${spring.kafka.topic.ticket-used}")
    private String ticketUsedTopic;

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
            log.error("결제 요청을 보내는 중 오류가 발생했습니다 - orderId: {}", event.getOrderId(),
                e);
            throw e;
        }
        log.info("결제 요청 이벤트가 성공적으로 전송되었습니다. orderId: {}", event.getOrderId());
    }

    public void sendCouponUsed(UUID userCouponId) {

        try {
            CompletableFuture<SendResult<String, String>> future =
                stringKafkaTemplate.send(
                    couponUsedTopic,
                    userCouponId.toString()
                );

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("쿠폰 사용 이벤트 전송 성공 - userCouponId: {}, partition: {}, offset: {}",
                        userCouponId,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                } else {
                    log.error("쿠폰 사용 이벤트 전송 실패 - userCouponId: {}", userCouponId, ex);
                }
            });
        } catch (Exception e) {
            log.error("쿠폰 사용 이벤트 전송 중 오류 발생 - userCouponId: {}", userCouponId, e);
            throw e;
        }
    }
    public void sendTicketId(List<UUID> ticketIds) throws JsonProcessingException {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            String ticketIdsJson = objectMapper.writeValueAsString(ticketIds);


            CompletableFuture<SendResult<String, String>> future =
                stringKafkaTemplate.send(
                    ticketUsedTopic,
                    ticketIdsJson
                );
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("티켓 발행 이벤트 전송 성공 - ticketId: {}, partition: {}, offset: {}",
                        ticketIdsJson,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                } else {
                    log.error("티켓 발행 이벤트 전송 실패 - ticketId: {}", ticketIdsJson, ex);
                }
            });
        } catch (Exception e) {
            log.error("티켓 발행 이벤트 전송 중 오류 발생 - ticketId: {}", ticketIds, e);
            throw e;
        }
    }
}
