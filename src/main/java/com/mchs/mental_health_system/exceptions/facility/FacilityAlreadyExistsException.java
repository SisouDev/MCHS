package com.mchs.mental_health_system.exceptions.facility;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class FacilityAlreadyExistsException extends BusinessException {
  public FacilityAlreadyExistsException(String message) {
    super(message);
  }
}