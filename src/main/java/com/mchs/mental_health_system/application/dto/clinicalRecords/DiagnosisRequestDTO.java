package com.mchs.mental_health_system.application.dto.clinicalRecords;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DiagnosisRequestDTO(
        String diagnosisCode,
        @NotBlank String description,
        @NotNull LocalDate diagnosisDate,
        boolean isPrimary
) {
}
