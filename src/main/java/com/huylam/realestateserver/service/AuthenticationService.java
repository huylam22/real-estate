package com.huylam.realestateserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huylam.realestateserver.config.auth.JwtService;
import com.huylam.realestateserver.entity.auth.AuthenticationRequest;
import com.huylam.realestateserver.entity.auth.AuthenticationResponse;
import com.huylam.realestateserver.entity.auth.RegisterRequest;
import com.huylam.realestateserver.entity.token.Token;
import com.huylam.realestateserver.entity.token.TokenType;
import com.huylam.realestateserver.entity.user.Role;
import com.huylam.realestateserver.entity.user.User;
import com.huylam.realestateserver.repository.auth.TokenRepository;
import com.huylam.realestateserver.repository.auth.UserRepository;
import com.huylam.realestateserver.service.DTO.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  @Autowired
  private final UserRepository userRepository;

  @Autowired
  private final TokenRepository tokenRepository;

  @Autowired
  private final PasswordEncoder passwordEncoder;

  @Autowired
  private final JwtService jwtService;

  @Autowired
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(
    @Valid RegisterRequest request,
    BindingResult bindingResult
  ) {
    if (bindingResult.hasErrors()) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Invalid request"
      );
    }
    // Check if the email is already registered
    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
      throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Email already registered"
      );
    }

    Role defaultRole = Role.USER;
    Role userRole = request.getRole() != null ? request.getRole() : defaultRole;
    var user = User
      .builder()
      .firstname(request.getFirstname())
      .lastname(request.getLastname())
      .email(request.getEmail())
      .password(passwordEncoder.encode(request.getPassword()))
      .role(userRole)
      .build();
    var savedUser = userRepository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    UserDTO userDTO = new UserDTO(user); // Create UserDTO from User object

    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse
      .builder()
      .user(userDTO)
      .accessToken(jwtToken)
      .refreshToken(refreshToken)
      .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          request.getEmail(),
          request.getPassword()
        )
      );
    } catch (AuthenticationException e) {
      // Handle authentication failure, e.g., incorrect user or password
      throw new AuthenticationServiceException("Invalid email or password");
    }

    var user = userRepository
      .findByEmail(request.getEmail())
      .orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
      );

    UserDTO userDTO = new UserDTO(user); // Create UserDTO from User object

    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);

    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);

    return AuthenticationResponse
      .builder()
      .user(userDTO) // Include the userDTO in the response
      .accessToken(jwtToken)
      .refreshToken(refreshToken)
      .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token
      .builder()
      .user(user)
      .token(jwtToken)
      .tokenType(TokenType.BEARER)
      .expired(false)
      .revoked(false)
      .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty()) return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.userRepository.findByEmail(userEmail).orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var userDTO = new UserDTO(user);
        var authResponse = AuthenticationResponse
          .builder()
          .user(userDTO)
          .accessToken(accessToken)
          .refreshToken(refreshToken)
          .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
