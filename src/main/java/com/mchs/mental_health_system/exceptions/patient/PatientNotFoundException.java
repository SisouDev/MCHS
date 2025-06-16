package com.mchs.mental_health_system.exceptions.patient;

import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;

public class PatientNotFoundException extends ResourceNotFoundException {
  public PatientNotFoundException(Long id) {
    super("Patient not found with ID: " + id);
  }
}