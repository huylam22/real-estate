package com.huylam.realestateserver.config.auth;

import org.springframework.security.authentication.AuthenticationServiceException;

public class PasswordValidationException
  extends AuthenticationServiceException {

  public PasswordValidationException(String message) {
    super(message);
  }
}
