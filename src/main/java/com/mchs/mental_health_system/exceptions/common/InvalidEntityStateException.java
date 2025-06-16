package com.mchs.mental_health_system.exceptions.common;

public class InvalidEntityStateException extends RuntimeException {
  public InvalidEntityStateException(String message) {
    super(message);
  }
}