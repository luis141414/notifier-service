package com.realtimenotifications.notifier_service.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRuleDTO {
    private String userName;
    private String event;
    private String field;
    private String operator;
    private double threshold;
    private String timeWindow;
    private String channel;
    private String recipient;
}
