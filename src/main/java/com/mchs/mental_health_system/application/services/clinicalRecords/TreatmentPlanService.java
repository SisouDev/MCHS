package com.mchs.mental_health_system.application.services.clinicalRecords;

import com.mchs.mental_health_system.application.dto.clinicalRecords.TreatmentPlanRequestDTO;
import com.mchs.mental_health_system.application.dto.clinicalRecords.TreatmentPlanResponseDTO;
import com.mchs.mental_health_system.domain.model.enums.clinicalRecords.TreatmentPlanStatus;

public interface TreatmentPlanService {

    TreatmentPlanResponseDTO createTreatmentPlan(Long patientId, TreatmentPlanRequestDTO requestDTO);

    TreatmentPlanResponseDTO updateTreatmentPlanStatus(Long planId, TreatmentPlanStatus newStatus);

    TreatmentPlanResponseDTO getTreatmentPlanForPatient(Long patientId);
}