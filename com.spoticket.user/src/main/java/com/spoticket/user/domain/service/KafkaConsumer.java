package com.spoticket.user.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spoticket.user.domain.model.entity.UserCoupon;
import com.spoticket.user.domain.repository.UserCouponRepository;
import com.spoticket.user.global.exception.CustomException;
import com.spoticket.user.global.exception.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.spoticket.user.global.exception.ErrorStatus.COUPON_DEACTIVATE;
import static com.spoticket.user.global.exception.ErrorStatus.COUPON_NOT_FOUND;

@Slf4j
@Service
public class KafkaConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserCouponRepository userCouponRepository;

    public KafkaConsumer(UserCouponRepository userCouponRepository) {
        this.userCouponRepository = userCouponRepository;
    }

    @Transactional
    @KafkaListener(topics = {"use-coupon"})
    public void useCouponTopic(ConsumerRecord<String, String> consumerRecord) throws ParseException {
        log.info("👀👀 [KafkaConsumer] use-coupon Consumed 👀👀");

        // Kafka 메시지 정보
        int partition = consumerRecord.partition();
        long topicOffset = consumerRecord.offset();
        String topicName = consumerRecord.topic();
        String topicMessage = consumerRecord.value();

        log.info("Partition: {}, Offset: {}, Topic: {}, Message: {}", partition, topicOffset, topicName, topicMessage);

        try {
            // JSON 파싱
            JsonNode jsonNode = objectMapper.readTree(topicMessage);
            UUID couponId = UUID.fromString(jsonNode.get("userCouponId").asText()); // "couponId" 값 추출
            log.info("Extracted couponId: {}", couponId);

            UserCoupon userCoupon = userCouponRepository.findById(couponId).orElseThrow(
                    () -> new CustomException(COUPON_NOT_FOUND)
            );

            if(!userCoupon.getCoupon().getIsActive()){
                throw new CustomException(COUPON_DEACTIVATE);
            }

            userCoupon.use();

        } catch (Exception e) {
            log.error("Error parsing Kafka message: {}", topicMessage, e);
        }
    }
}