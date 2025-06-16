package com.mchs.mental_health_system.application.mappers.others;

import com.mchs.mental_health_system.application.dto.others.AuditLogDTO;
import com.mchs.mental_health_system.domain.model.entities.others.AuditLog;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuditLogMapper {

    AuditLogDTO toDTO(AuditLog auditLog);

    List<AuditLogDTO> toDTOList(List<AuditLog> auditLogs);
}