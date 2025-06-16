package com.mchs.mental_health_system.exceptions.user;

import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;

public class ProfessionalNotFoundException extends ResourceNotFoundException {
  public ProfessionalNotFoundException(Long id) {
    super("Professional not found with ID: " + id);
  }
}