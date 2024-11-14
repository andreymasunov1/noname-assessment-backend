package org.dci.assecorassessmentbackend.exception;

import java.time.LocalDateTime;

public class ApiException {
  private final String message;
  private final String details;
  private final LocalDateTime timestamp;

  public ApiException(String message, String details) {
    this.message = message;
    this.details = details;
    this.timestamp = LocalDateTime.now();
  }

  public String getMessage() {
    return message;
  }

  public String getDetails() {
    return details;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }
}
