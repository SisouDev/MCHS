package com.mchs.mental_health_system.exceptions.clinicalRecords;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class PrimaryDiagnosisAlreadyExistsException extends BusinessException {
  public PrimaryDiagnosisAlreadyExistsException(Long patientId) {
    super("The patient with ID " + patientId + " already has a defined main diagnosis.");
  }
}