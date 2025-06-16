package com.mchs.mental_health_system.application.dto.others;
import java.time.LocalDateTime;

public record AuditLogDTO(
        Long id,
        LocalDateTime timestamp,
        String username,
        String action,
        String details
) {
}