package com.mchs.mental_health_system.exceptions.patient;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class PatientAlreadyAssignedToFacilityException extends BusinessException {
  public PatientAlreadyAssignedToFacilityException(Long patientId, Long facilityId) {
    super("The patient with ID " + patientId + " is already associated with a health unit (ID: " + facilityId + ").");
  }
}