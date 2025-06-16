package com.mchs.mental_health_system.exceptions.patient;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class PatientAlreadyExistsException extends BusinessException {
  public PatientAlreadyExistsException(String message) {
    super(message);
  }
}