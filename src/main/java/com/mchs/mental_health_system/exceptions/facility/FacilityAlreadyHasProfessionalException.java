package com.mchs.mental_health_system.exceptions.facility;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class FacilityAlreadyHasProfessionalException extends BusinessException {
  public FacilityAlreadyHasProfessionalException(Long professionalId, Long facilityId) {
    super("The professional with ID " + professionalId + " is already booked into the unit with ID " + facilityId);
  }
}