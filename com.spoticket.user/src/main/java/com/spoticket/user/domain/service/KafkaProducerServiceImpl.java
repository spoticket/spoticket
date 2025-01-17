package com.spoticket.user.domain.service;

import jakarta.annotation.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService{

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendTopic(String topic, byte[] data) {
        kafkaTemplate.send(topic, data);
    }

    @Override
    public void sendTopic(String topic, Object data) {
        kafkaTemplate.send(topic, data);
    }

    @Override
    public void sendMultiTopic(String topicArr, Object data) {
    }
}
