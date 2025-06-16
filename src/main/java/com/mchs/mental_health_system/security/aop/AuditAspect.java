package com.mchs.mental_health_system.security.aop;

import com.mchs.mental_health_system.domain.model.entities.others.AuditLog;
import com.mchs.mental_health_system.domain.model.shared.functions.Auditable;
import com.mchs.mental_health_system.domain.repositories.others.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;

    @Pointcut("@annotation(auditable)")
    public void auditableMethod(Auditable auditable) {}

    @AfterReturning(pointcut = "auditableMethod(auditable)", returning = "result")
    public void logAudit(JoinPoint joinPoint, Auditable auditable, Object result) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        String details = "Method " + joinPoint.getSignature().toShortString() +
                " was called with args: " + Arrays.stream(joinPoint.getArgs()).map(Object::toString).collect(Collectors.joining(", "));

        AuditLog log = new AuditLog();
        log.setTimestamp(LocalDateTime.now());
        log.setUsername(username);
        log.setAction(auditable.action());
        log.setDetails(details);

        auditLogRepository.save(log);
    }
}