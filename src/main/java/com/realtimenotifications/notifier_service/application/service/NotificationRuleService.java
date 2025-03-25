package com.realtimenotifications.notifier_service.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realtimenotifications.notifier_service.application.dto.NotificationRuleDTO;
import com.realtimenotifications.notifier_service.domain.model.NotificationRule;
import com.realtimenotifications.notifier_service.domain.repository.NotificationRuleRepository;
import com.realtimenotifications.notifier_service.infrastructure.messaging.KafkaNotificationProducer;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationRuleService {

    private final NotificationRuleRepository ruleRepository;
    private final KafkaNotificationProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    public NotificationRuleService(NotificationRuleRepository ruleRepository,
                                   KafkaNotificationProducer kafkaProducer,
                                   ObjectMapper objectMapper) {
        this.ruleRepository = ruleRepository;
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }

    public NotificationRule createRule(NotificationRuleDTO dto) {
        NotificationRule rule = NotificationRule.builder()
                .userName(dto.getUserName())
                .event(dto.getEvent())
                .field(dto.getField())
                .operator(dto.getOperator())
                .threshold(dto.getThreshold())
                .timeWindow(dto.getTimeWindow())
                .channel(dto.getChannel())
                .recipient(dto.getRecipient())
                .build();

        NotificationRule savedRule = ruleRepository.save(rule);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(savedRule);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // Envía la regla al topic Kafka
        kafkaProducer.sendNotificationEvent(json);

        return savedRule;
    }

    public List<NotificationRule> getRulesByUser(String userName) {
        return ruleRepository.findByUserName(userName);
    }

    public void deleteRule(Long id) {
        ruleRepository.deleteById(id);
    }
}
