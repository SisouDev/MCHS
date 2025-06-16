package com.mchs.mental_health_system.exceptions.user;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class InvalidPasswordException extends BusinessException {
  public InvalidPasswordException() {
    super("The password provided is incorrect.");
  }
}