package com.mchs.mental_health_system.application.dto.patient;

public record PatientSummaryDTO(
        Long id,
        String fullName,
        String documentNumber
) {
}
