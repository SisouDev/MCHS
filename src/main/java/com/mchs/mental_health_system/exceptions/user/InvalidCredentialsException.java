package com.mchs.mental_health_system.exceptions.user;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class InvalidCredentialsException extends BusinessException {
  public InvalidCredentialsException() {
    super("Invalid credentials. Please check your username and password.");
  }
}