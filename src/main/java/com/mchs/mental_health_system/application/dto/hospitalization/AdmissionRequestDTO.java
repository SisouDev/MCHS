package com.mchs.mental_health_system.application.dto.hospitalization;

import com.mchs.mental_health_system.domain.model.enums.hospitalization.AdmissionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AdmissionRequestDTO(
        @NotNull AdmissionType type,
        @NotNull LocalDateTime admissionDate,
        @NotBlank String reason,
        String ward
) {
}
