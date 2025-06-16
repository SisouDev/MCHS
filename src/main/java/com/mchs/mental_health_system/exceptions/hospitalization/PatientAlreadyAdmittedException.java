package com.mchs.mental_health_system.exceptions.hospitalization;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class PatientAlreadyAdmittedException extends BusinessException {
    public PatientAlreadyAdmittedException(Long patientId) {
        super("Invalid operation. The patient with ID " + patientId + " is already admitted");
    }
}