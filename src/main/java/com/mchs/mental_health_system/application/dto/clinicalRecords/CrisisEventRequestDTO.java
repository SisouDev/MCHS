package com.mchs.mental_health_system.application.dto.clinicalRecords;

import com.mchs.mental_health_system.domain.model.enums.clinicalRecords.SeverityLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CrisisEventRequestDTO(
        @NotNull Long patientId,
        @NotNull LocalDateTime eventDateTime,
        @NotBlank String description,
        @NotNull SeverityLevel severity,
        String actionTaken,
        String outcome,
        @NotNull Long reportedById
) {
}
