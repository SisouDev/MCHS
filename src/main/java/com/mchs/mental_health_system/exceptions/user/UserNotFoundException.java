package com.mchs.mental_health_system.exceptions.user;

import com.mchs.mental_health_system.exceptions.common.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
  public UserNotFoundException(Long id) {
    super("User not found with ID: " + id);
  }

  public UserNotFoundException(String username) {
    super("User not found with username: " + username);
  }
}