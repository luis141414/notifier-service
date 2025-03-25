package com.realtimenotifications.notifier_service.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realtimenotifications.notifier_service.domain.model.NotificationRule;
import com.realtimenotifications.notifier_service.domain.service.NotificationSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@Service
public class BtcPriceMonitorService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;
    private final NotificationSender notificationSender;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
    private final Map<Long, ScheduledFuture<?>> activeTasks = new ConcurrentHashMap<>();

    public BtcPriceMonitorService(NotificationSender notificationSender,
                                  ObjectMapper objectMapper) {
        this.notificationSender = notificationSender;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "test-topic", groupId = "my-group")
    public void listenRule(String message) {
        try {
            NotificationRule rule = objectMapper.readValue(message, NotificationRule.class);
            if ("BTC_PRICE".equalsIgnoreCase(rule.getEvent()) && !activeTasks.containsKey(rule.getId())) {
                log.info("🎯 Añadiendo regla BTC activa con monitorización: {}", rule);
                ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(
                        () -> checkRule(rule),
                        0,
                        60,
                        TimeUnit.SECONDS
                );
                activeTasks.put(rule.getId(), task);
            }
        } catch (Exception e) {
            log.error("❌ Error al procesar la regla desde Kafka: {}", e.getMessage());
        }
    }

    private void checkRule(NotificationRule rule) {
        Double btcPrice = fetchBtcPrice();
        if (btcPrice == null) return;

        log.info("📈 [Regla {}] Precio actual de BTC: {}", rule.getId(), btcPrice);

        boolean conditionMet = switch (rule.getOperator()) {
            case ">" -> btcPrice > rule.getThreshold();
            case "<" -> btcPrice < rule.getThreshold();
            case "=" -> btcPrice.equals(rule.getThreshold());
            default -> false;
        };

        if (conditionMet) {
            log.info("✅ Regla cumplida para {}: {}", rule.getUserName(), rule);
            if ("EMAIL".equalsIgnoreCase(rule.getChannel())) {
                notificationSender.send(
                        rule.getRecipient(),
                        "Regla cumplida",
                        "El precio del BTC es: " + btcPrice
                );
            }
            // Cancelar la tarea si solo queremos notificar una vez
            cancelRule(rule.getId());
        }
    }

    private Double fetchBtcPrice() {
        try {
            String url = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd";
            var response = restTemplate.getForObject(url, String.class);
            return objectMapper.readTree(response).path("bitcoin").path("usd").asDouble();
        } catch (Exception e) {
            log.error("❌ Error al obtener el precio del BTC: {}", e.getMessage());
            return null;
        }
    }

    private void cancelRule(Long ruleId) {
        ScheduledFuture<?> task = activeTasks.remove(ruleId);
        if (task != null) {
            task.cancel(true);
            log.info("🛑 Monitorización detenida para regla: {}", ruleId);
        }
    }
}