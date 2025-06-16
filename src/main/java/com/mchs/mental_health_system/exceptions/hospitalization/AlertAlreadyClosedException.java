package com.mchs.mental_health_system.exceptions.hospitalization;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class AlertAlreadyClosedException extends BusinessException {
  public AlertAlreadyClosedException(Long alertId) {
    super("Alert ID " + alertId + " has already been closed and cannot be modified.");
  }
}