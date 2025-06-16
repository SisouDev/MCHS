package com.mchs.mental_health_system.exceptions.clinicalRecords;

import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;

public class EventNotFoundException extends ResourceNotFoundException {
  public EventNotFoundException(Long id) {
    super("Event not found with ID: " + id);
  }
}