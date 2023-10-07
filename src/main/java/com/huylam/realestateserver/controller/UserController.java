package com.huylam.realestateserver.controller;

import com.huylam.realestateserver.entity.Province;
import com.huylam.realestateserver.entity.user.User;
import com.huylam.realestateserver.repository.auth.UserRepository;
import com.huylam.realestateserver.service.AuthenticationService;
import com.huylam.realestateserver.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.http.HttpHeaders;
import org.hibernate.mapping.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
@PreAuthorize("hasRole('ADMIN', 'USER')")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @GetMapping
  @PreAuthorize("hasAuthority('admin:read')")
  public ResponseEntity<ArrayList<User>> getAllUsers() {
    try {
      return new ResponseEntity<>(
        userService.getAllUsersService(),
        HttpStatus.OK
      );
    } catch (Exception e) {}
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('admin:read')")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    Optional<User> userOptional = userService.getUserById(id);
    return userOptional
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping("/info")
  @PreAuthorize("hasAuthority('user:read') or hasAuthority('admin:read')")
  public ResponseEntity<User> getUserInfoByAccessToken(
    HttpServletRequest request
  ) {
    try {
      User user = userService.getUserInfoByAccessToken(request);
      return new ResponseEntity<>(user, HttpStatus.OK);
    } catch (Exception e) {
      // Handle exceptions appropriately, e.g., log the error
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }
  // @GetMapping("/user-info")
  // public ResponseEntity<User> getUserInfoByAccessToken(
  //   @CookieValue(
  //     value = "app_access_token",
  //     defaultValue = ""
  //   ) String accessToken
  // ) {
  //   try {
  //     // Check if the access token is present in the cookie
  //     if (!accessToken.isEmpty()) {
  //       User user = userService.getUserInfoByAccessTokenCookie(accessToken);

  //       if (user != null) {
  //         return new ResponseEntity<>(user, HttpStatus.OK);
  //       }
  //     }

  //     // Handle invalid access token or user not found
  //     return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  //   } catch (Exception e) {
  //     // Handle exceptions appropriately, e.g., log the error
  //     return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
  //   }
  // }
  // @GetMapping("/get-cookies")
  // public String readAllCookies(HttpServletRequest request) {
  //   Cookie[] cookies = request.getCookies();
  //   if (cookies != null) {
  //     return Arrays
  //       .stream(cookies)
  //       .map(c -> c.getName() + "=" + c.getValue())
  //       .collect(Collectors.joining(", "));
  //   }

  //   return "No cookies";
  // }
}
