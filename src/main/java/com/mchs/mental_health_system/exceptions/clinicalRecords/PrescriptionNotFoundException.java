package com.mchs.mental_health_system.exceptions.clinicalRecords;

import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;

public class PrescriptionNotFoundException extends ResourceNotFoundException {
  public PrescriptionNotFoundException(Long id) {
    super("Prescription not found with ID: " + id);
  }
}