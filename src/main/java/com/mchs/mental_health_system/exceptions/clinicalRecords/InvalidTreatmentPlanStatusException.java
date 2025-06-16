package com.mchs.mental_health_system.exceptions.clinicalRecords;

import com.mchs.mental_health_system.domain.model.enums.clinicalRecords.TreatmentPlanStatus;
import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class InvalidTreatmentPlanStatusException extends BusinessException {
  public InvalidTreatmentPlanStatusException(String message) {
    super(message);
  }

  public InvalidTreatmentPlanStatusException(TreatmentPlanStatus expected, TreatmentPlanStatus actual) {
    super("Invalid action. The treatment plan should have status " + expected + ", but it has status " + actual + ".");
  }
}