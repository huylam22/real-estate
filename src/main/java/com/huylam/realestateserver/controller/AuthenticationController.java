package com.huylam.realestateserver.controller;

import com.huylam.realestateserver.entity.auth.AuthenticationRequest;
import com.huylam.realestateserver.entity.auth.AuthenticationResponse;
import com.huylam.realestateserver.entity.auth.RegisterRequest;
import com.huylam.realestateserver.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
    @Valid @RequestBody RegisterRequest request,
    BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      // Handle validation errors, return appropriate response
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Invalid request"
      );
    }
    AuthenticationResponse response = service.register(request, bindingResult);

    return ResponseEntity.ok(response);
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
    @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }
}
