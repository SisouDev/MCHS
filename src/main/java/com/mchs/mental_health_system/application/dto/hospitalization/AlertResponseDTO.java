package com.mchs.mental_health_system.application.dto.hospitalization;

import com.mchs.mental_health_system.domain.model.enums.hospitalization.AlertStatus;
import com.mchs.mental_health_system.domain.model.enums.hospitalization.AlertType;

import java.time.LocalDateTime;

public record AlertResponseDTO(
        Long id,
        AlertType type,
        AlertStatus status,
        String message,
        LocalDateTime createdAt,
        Long patientId,
        String patientFullName,
        Long assignedToProfessionalId,
        String assignedToProfessionalName
) {
}
