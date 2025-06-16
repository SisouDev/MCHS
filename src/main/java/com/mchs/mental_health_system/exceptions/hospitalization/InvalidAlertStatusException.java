package com.mchs.mental_health_system.exceptions.hospitalization;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class InvalidAlertStatusException extends BusinessException {
  public InvalidAlertStatusException(String message) {
    super(message);
  }
}