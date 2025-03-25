package com.realtimenotifications.notifier_service.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_rules")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;   // The  username of the user who created the rule  
    private String event;      // The type of event to monitor (e.g., "BTC_PRICE", "SERVER_STATUS")  

    private String field;      // The specific field within the event to evaluate (e.g., "price", "status")  
    private String operator;   // The comparison operator for the rule (e.g., "<", ">", "=")  
    private double threshold;  // The threshold value to trigger the notification (e.g., 50000.0 for BTC price)  

    private String timeWindow; // The time window for rule evaluation (e.g., "1H", "24H", "7D")  
    private String channel;    // The notification channel (e.g., "EMAIL", "SMS", "TELEGRAM")  
    private String recipient;  // The recipient who will receive the notification (email, phone number, etc.)  

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();  // The timestamp when the rule was created  
}
