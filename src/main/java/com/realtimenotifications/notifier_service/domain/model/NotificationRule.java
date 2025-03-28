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

    private String userName;
    private String event;

    private String field;
    private String operator;
    private double threshold;

    private String timeWindow;
    private String channel;
    private String recipient;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}