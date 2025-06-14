package com.mchs.mental_health_system.application.dto.clinicalRecords;

import java.time.LocalDate;

public record DiagnosisResponseDTO(
        Long id,
        Long patientId,
        String diagnosisCode,
        String description,
        LocalDate diagnosisDate,
        boolean isPrimary
) {
}
