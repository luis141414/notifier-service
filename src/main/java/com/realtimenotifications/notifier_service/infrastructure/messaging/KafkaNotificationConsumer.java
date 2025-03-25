package com.realtimenotifications.notifier_service.infrastructure.messaging;

import org.springframework.kafka.annotation.KafkaListener; 
import org.springframework.stereotype.Service;

@Service
public class KafkaNotificationConsumer {

    @KafkaListener(topics = "test-topic", groupId = "my-group")
    public void consumeNotificationEvent(String message) {
        System.out.println("Received notification event: " + message);
        // Aquí puedes agregar lógica para procesar la notificación (por ejemplo, enviarla a Telegram)
    }
}
