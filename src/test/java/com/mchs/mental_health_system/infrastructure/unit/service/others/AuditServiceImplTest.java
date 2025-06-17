package com.mchs.mental_health_system.infrastructure.unit.service.others;

import com.mchs.mental_health_system.application.dto.others.AuditLogDTO;
import com.mchs.mental_health_system.application.mappers.others.AuditLogMapper;
import com.mchs.mental_health_system.domain.model.entities.others.AuditLog;
import com.mchs.mental_health_system.domain.repositories.others.AuditLogRepository;
import com.mchs.mental_health_system.infrastructure.services.others.AuditServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private AuditLogMapper auditLogMapper;

    @InjectMocks
    private AuditServiceImpl auditService;

    @Test
    @DisplayName("findLogsByUsername deve retornar lista de logs para um usuário")
    void findLogsByUsername_shouldReturnLogsForUser() {
        String username = "testuser";
        AuditLog logEntity = new AuditLog();
        List<AuditLog> entityList = List.of(logEntity);

        AuditLogDTO logDTO = new AuditLogDTO(1L, LocalDateTime.now(), username, "LOGIN", "Success");
        List<AuditLogDTO> dtoList = List.of(logDTO);

        when(auditLogRepository.findByUsernameOrderByTimestampDesc(username)).thenReturn(entityList);
        when(auditLogMapper.toDTOList(entityList)).thenReturn(dtoList);

        List<AuditLogDTO> result = auditService.findLogsByUsername(username);

        assertThat(result).isEqualTo(dtoList);
        verify(auditLogRepository).findByUsernameOrderByTimestampDesc(username);
        verify(auditLogMapper).toDTOList(entityList);
    }

    @Test
    @DisplayName("findLogsByUsername deve retornar lista vazia se não houver logs")
    void findLogsByUsername_shouldReturnEmptyList_whenNoLogsFound() {
        String username = "nonexistent_user";
        when(auditLogRepository.findByUsernameOrderByTimestampDesc(username)).thenReturn(Collections.emptyList());
        when(auditLogMapper.toDTOList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<AuditLogDTO> result = auditService.findLogsByUsername(username);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findLogsByPeriod deve retornar lista de logs para um período")
    void findLogsByPeriod_shouldReturnLogsForPeriod() {
        LocalDateTime start = LocalDateTime.of(2025, 6, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 17, 0, 0);
        List<AuditLog> entityList = List.of(new AuditLog());
        List<AuditLogDTO> dtoList = List.of(new AuditLogDTO(1L, LocalDateTime.now(), "user", "ACTION", "details"));

        when(auditLogRepository.findByTimestampBetweenOrderByTimestampDesc(start, end)).thenReturn(entityList);
        when(auditLogMapper.toDTOList(entityList)).thenReturn(dtoList);

        List<AuditLogDTO> result = auditService.findLogsByPeriod(start, end);

        assertThat(result).isEqualTo(dtoList);
        verify(auditLogRepository).findByTimestampBetweenOrderByTimestampDesc(start, end);
        verify(auditLogMapper).toDTOList(entityList);
    }
}