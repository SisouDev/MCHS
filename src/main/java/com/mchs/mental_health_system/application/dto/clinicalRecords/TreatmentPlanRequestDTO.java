package com.mchs.mental_health_system.application.dto.clinicalRecords;

import com.mchs.mental_health_system.domain.model.enums.clinicalRecords.TreatmentPlanStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TreatmentPlanRequestDTO(
        Long supervisorId,
        @NotNull TreatmentPlanStatus status,
        @NotBlank String objectives,
        @NotNull @FutureOrPresent LocalDate startDate,
        @Future LocalDate expectedEndDate
) {
}
