package com.cs203.core.exception;

public class JwtCreationException extends RuntimeException {
  public JwtCreationException(String message, Throwable cause) {
    super(message, cause);
  }
}
