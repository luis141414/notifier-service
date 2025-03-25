package com.realtimenotifications.notifier_service.domain.repository;

import com.realtimenotifications.notifier_service.domain.model.NotificationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRuleRepository extends JpaRepository<NotificationRule, Long> {
    List<NotificationRule> findByUserName(String userName);
    List<NotificationRule> findByEvent(String event);
}
