package com.mchs.mental_health_system.exceptions.hospitalization;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class AdmissionConflictException extends BusinessException {
  public AdmissionConflictException(String message) {
    super(message);
  }
}