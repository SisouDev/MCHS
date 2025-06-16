package com.mchs.mental_health_system.exceptions.facility;

import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;

public class FacilityNotFoundException extends ResourceNotFoundException {
  public FacilityNotFoundException(Long id) {
    super("Health Unit not found with ID: " + id);
  }
}