package com.mchs.mental_health_system.exceptions.clinicalRecords;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class DuplicateTreatmentPlanException extends BusinessException {
    public DuplicateTreatmentPlanException(Long patientId) {
        super("The patient with ID " + patientId + " already have an active Treatment Plan.");
    }
}