package com.mchs.mental_health_system.application.services.config;

import com.mchs.mental_health_system.application.dto.others.AuditLogDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface AuditService {
    List<AuditLogDTO> findLogsByUsername(String username);
    List<AuditLogDTO> findLogsByPeriod(LocalDateTime start, LocalDateTime end);
}