package com.mchs.mental_health_system.exceptions.clinicalRecords;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class UnsupportedEventTypeException extends BusinessException {
  public UnsupportedEventTypeException(String type) {
    super("The type of event '" + type + "' is not supported by this operation.");
  }
}