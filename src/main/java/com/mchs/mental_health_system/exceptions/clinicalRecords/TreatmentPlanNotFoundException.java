package com.mchs.mental_health_system.exceptions.clinicalRecords;

import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;

public class TreatmentPlanNotFoundException extends ResourceNotFoundException {
  public TreatmentPlanNotFoundException(Long id) {
    super("Treatment Plan not found with ID: " + id);
  }
}