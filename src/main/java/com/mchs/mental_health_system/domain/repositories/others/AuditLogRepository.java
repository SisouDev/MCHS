package com.mchs.mental_health_system.domain.repositories.others;

import com.mchs.mental_health_system.domain.model.entities.others.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUsername(String username);
    List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}