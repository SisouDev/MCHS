package com.mchs.mental_health_system.exceptions.common;

public class BusinessException extends RuntimeException {
  public BusinessException(String message) {
    super(message);
  }
}