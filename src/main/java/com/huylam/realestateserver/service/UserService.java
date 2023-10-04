package com.huylam.realestateserver.service;

import com.huylam.realestateserver.config.auth.JwtService;
import com.huylam.realestateserver.entity.auth.AuthenticationResponse;
import com.huylam.realestateserver.entity.user.User;
import com.huylam.realestateserver.repository.auth.UserRepository;
import com.huylam.realestateserver.service.DTO.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

  @Autowired
  private final UserRepository userRepository;

  @Autowired
  private final JwtService jwtService;

  public ArrayList<User> getAllUsersService() {
    ArrayList<User> userList = new ArrayList<User>();
    userRepository.findAll().forEach(userList::add);
    return userList;
  }

  public Optional<User> getUserById(Long userId) {
    return userRepository.findById(userId);
  }

  public User getUserInfoByAccessToken(HttpServletRequest request) {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String accessToken;
    final String userEmail;

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      // Handle invalid or missing authorization header
      throw new RuntimeException("Invalid or missing authorization header");
    }

    accessToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(accessToken);

    if (userEmail != null) {
      var user = userRepository
        .findByEmail(userEmail)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

      if (jwtService.isTokenValid(accessToken, user)) {
        return user;
      }
    }

    // Handle invalid access token or user not found
    throw new RuntimeException("Invalid access token or user not found");
  }
}
