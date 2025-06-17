package com.mchs.mental_health_system.application.controller.other;

import com.mchs.mental_health_system.application.dto.others.AuditLogDTO;
import com.mchs.mental_health_system.application.services.config.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/audit-logs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    public ResponseEntity<List<AuditLogDTO>> findLogs(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        if (StringUtils.hasText(username)) {
            return ResponseEntity.ok(auditService.findLogsByUsername(username));
        }

        if (start != null && end != null) {
            return ResponseEntity.ok(auditService.findLogsByPeriod(start, end));
        }

        return ResponseEntity.ok(Collections.emptyList());
    }
}