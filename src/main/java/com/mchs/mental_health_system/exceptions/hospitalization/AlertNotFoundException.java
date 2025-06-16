package com.mchs.mental_health_system.exceptions.hospitalization;

import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;

public class AlertNotFoundException extends ResourceNotFoundException {
  public AlertNotFoundException(Long id) {
    super("Alert not found with ID: " + id);
  }
}