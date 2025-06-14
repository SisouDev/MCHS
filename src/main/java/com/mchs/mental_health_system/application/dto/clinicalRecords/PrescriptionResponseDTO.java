package com.mchs.mental_health_system.application.dto.clinicalRecords;

import java.time.LocalDate;

public record PrescriptionResponseDTO(
        Long id,
        Long consultationSessionId,
        String medicationName,
        String dosage,
        String instructions,
        LocalDate prescriptionDate,
        int durationInDays
) {
}
