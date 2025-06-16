package com.mchs.mental_health_system.infrastructure.services.others;

import com.mchs.mental_health_system.application.mappers.others.AuditLogMapper;
import com.mchs.mental_health_system.application.services.config.AuditService;
import com.mchs.mental_health_system.domain.repositories.others.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mchs.mental_health_system.application.dto.others.AuditLogDTO;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogDTO> findLogsByUsername(String username) {
        throw new UnsupportedOperationException("Not implemented yet. Please add findByUsername to AuditLogRepository.");
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLogDTO> findLogsByPeriod(LocalDateTime start, LocalDateTime end) {
        throw new UnsupportedOperationException("Not implemented yet. Please add findByTimestampBetween to AuditLogRepository.");
    }
}