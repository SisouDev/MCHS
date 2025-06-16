package com.mchs.mental_health_system.exceptions.user;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class UserRoleMismatchException extends BusinessException {
  public UserRoleMismatchException(String message) {
    super(message);
  }
}