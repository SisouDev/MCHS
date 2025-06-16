package com.mchs.mental_health_system.exceptions.user;

import com.mchs.mental_health_system.exceptions.common.BusinessException;

public class UsernameAlreadyExistsException extends BusinessException {
  public UsernameAlreadyExistsException(String username) {
    super("Username '" + username + "' is already in use.");
  }
}