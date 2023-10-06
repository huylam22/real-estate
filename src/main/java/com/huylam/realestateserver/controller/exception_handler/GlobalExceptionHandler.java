package com.huylam.realestateserver.controller.exception_handler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Map<String, String>> handleResponseStatusException(
    ResponseStatusException ex
  ) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("errorMessage", ex.getReason());
    return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<Map<String, String>> handleValidationException(
    ValidationException ex
  ) {
    // Extract the constraint violation message from the exception
    String errorMessage = ex.getCause().getMessage();
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("errorMessage", errorMessage);
    // Create a custom error response with the appropriate status code
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST; // Default to BAD_REQUEST
    if (ex instanceof ConstraintViolationException) {
      httpStatus = HttpStatus.UNPROCESSABLE_ENTITY; // Or choose an appropriate status for ConstraintViolationException
    }
    // Create a custom error response
    return ResponseEntity.status(httpStatus).body(errorResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, String>> handleConstraintViolationException(
    ConstraintViolationException ex
  ) {
    // Extract the constraint violation messages and create a custom error response
    Map<String, String> errorResponse = new HashMap<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      String errorMessage = violation.getMessage();
      errorResponse.put("errorMessage", errorMessage);
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(AuthenticationServiceException.class)
  public ResponseEntity<Map<String, String>> handleAuthenticationServiceException(
    AuthenticationServiceException ex
  ) {
    String errorMessage = ex.getMessage();
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("errorMessage", errorMessage);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, String>> handleRuntimeException(
    RuntimeException ex
  ) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("errorMessage", ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
  }
}
