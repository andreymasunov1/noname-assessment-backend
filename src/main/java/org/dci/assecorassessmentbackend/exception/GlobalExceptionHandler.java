package org.dci.assecorassessmentbackend.exception;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  // Handle ResourceNotFoundException
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiException> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
    ApiException apiError = new ApiException(ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
  }

  // Handle BadRequestException
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiException> handleBadRequestException(BadRequestException ex, WebRequest request) {
    ApiException apiError = new ApiException(ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
  }

  // Handle other exceptions (generic)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiException> handleGlobalException(Exception ex, WebRequest request) {
    ApiException apiError = new ApiException(ex.getMessage(), request.getDescription(false));
    return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
