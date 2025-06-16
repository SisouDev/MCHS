package com.mchs.mental_health_system.exceptions.hospitalization;

import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;

public class AdmissionNotFoundException extends ResourceNotFoundException {
  public AdmissionNotFoundException(Long id) {
    super("Admission not found with ID: " + id);
  }
}