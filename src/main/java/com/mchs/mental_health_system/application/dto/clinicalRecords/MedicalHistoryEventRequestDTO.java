package com.mchs.mental_health_system.application.dto.clinicalRecords;
import com.mchs.mental_health_system.domain.model.enums.patientManagement.ClinicalEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record MedicalHistoryEventRequestDTO(
        @NotNull Long patientId,
        @NotNull LocalDateTime eventDateTime,
        @NotBlank String description,
        @NotNull ClinicalEventType eventType
) {}