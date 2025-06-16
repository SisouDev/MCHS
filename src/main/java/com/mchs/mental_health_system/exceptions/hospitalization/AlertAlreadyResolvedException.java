package com.mchs.mental_health_system.exceptions.hospitalization;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class AlertAlreadyResolvedException extends BusinessException {
  public AlertAlreadyResolvedException(Long alertId) {
    super("The alert with ID " + alertId + " has already been resolved and cannot be modified.");
  }
}