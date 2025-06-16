package com.mchs.mental_health_system.exceptions.hospitalization;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

import java.time.LocalDateTime;

public class DischargeBeforeAdmissionException extends BusinessException {
  public DischargeBeforeAdmissionException(LocalDateTime admissionDate, LocalDateTime dischargeDate) {
    super("The date of medical discharge (" + dischargeDate + ") cannot be earlier than the date of hospitalization (" + admissionDate + ").");
  }
}