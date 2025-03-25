package com.realtimenotifications.notifier_service.domain.service;

public interface NotificationSender {
    void send(String recipient, String subject, String body);
}
