package com.mchs.mental_health_system.exceptions.patient;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class InvalidEmergencyContactException extends BusinessException {
  public InvalidEmergencyContactException(String message) {
    super("Invalid Emergency Contact: " + message);
  }
}