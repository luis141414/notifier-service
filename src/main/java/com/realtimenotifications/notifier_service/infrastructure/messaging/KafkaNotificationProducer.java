package com.realtimenotifications.notifier_service.infrastructure.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaNotificationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "test-topic";

    public KafkaNotificationProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotificationEvent(String message) {
        kafkaTemplate.send(TOPIC, message);
    }
}
