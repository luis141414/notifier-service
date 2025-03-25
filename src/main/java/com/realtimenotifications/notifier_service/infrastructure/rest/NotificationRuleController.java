package com.realtimenotifications.notifier_service.infrastructure.rest;

import com.realtimenotifications.notifier_service.application.service.NotificationRuleService;
import com.realtimenotifications.notifier_service.application.dto.NotificationRuleDTO;
import com.realtimenotifications.notifier_service.domain.model.NotificationRule;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class NotificationRuleController {

    private final NotificationRuleService ruleService;

    public NotificationRuleController(NotificationRuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public ResponseEntity<NotificationRule> createRule(@RequestBody NotificationRuleDTO dto) {
        return ResponseEntity.ok(ruleService.createRule(dto));
    }

    @GetMapping("/{userName}")
    public ResponseEntity<List<NotificationRule>> getRulesByUser(@PathVariable String userName) {
        return ResponseEntity.ok(ruleService.getRulesByUser(userName));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long id) {
        ruleService.deleteRule(id);
        return ResponseEntity.noContent().build();
    }
}
