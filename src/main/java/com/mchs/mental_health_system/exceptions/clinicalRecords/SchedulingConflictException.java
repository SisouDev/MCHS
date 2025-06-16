package com.mchs.mental_health_system.exceptions.clinicalRecords;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

import java.time.LocalDateTime;

public class SchedulingConflictException extends BusinessException {
  public SchedulingConflictException(String professionalName, LocalDateTime dateTime) {
    super("Scheduling conflict. The professional " + professionalName + " already has a session scheduled for " + dateTime);
  }
}