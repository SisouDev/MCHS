package com.mchs.mental_health_system.exceptions.clinicalRecords;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class InvalidPrescriptionDurationException extends BusinessException {
  public InvalidPrescriptionDurationException(String message) {
    super("Invalid prescription duration: " + message);
  }
}