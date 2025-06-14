package com.mchs.mental_health_system.application.dto.clinicalRecords;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ConsultationRequestDTO(
        @NotNull Long patientId,
        @NotNull Long healthProfessionalId,
        @NotNull LocalDateTime sessionDateTime,
        String clinicalNotes
) {
}
