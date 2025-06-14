package com.mchs.mental_health_system.application.dto.clinicalRecords;

import com.mchs.mental_health_system.domain.model.enums.clinicalRecords.TreatmentPlanStatus;

import java.time.LocalDate;

public record TreatmentPlanResponseDTO(
        Long id,
        TreatmentPlanStatus status,
        String objectives,
        LocalDate startDate,
        LocalDate expectedEndDate,
        Long patientId,
        String patientName,
        Long supervisorId,
        String supervisorName
) {
}
