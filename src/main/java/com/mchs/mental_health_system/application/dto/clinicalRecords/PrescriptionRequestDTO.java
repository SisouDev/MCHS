package com.mchs.mental_health_system.application.dto.clinicalRecords;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PrescriptionRequestDTO(
        @NotBlank String medicationName,
        String dosage,
        String instructions,
        @NotNull LocalDate prescriptionDate,
        int durationInDays
) {
}
