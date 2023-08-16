package com.huylam.realestateserver.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN', 'USER')")
public class DemoController {

  @GetMapping
  @PreAuthorize("hasAuthority('user:read')")
  public String get() {
    return "GET:: user controller";
  }

  @GetMapping("/all")
  public String getAll() {
    return "GET:: all controller";
  }

  @GetMapping("/test")
  @PreAuthorize("hasAuthority('admin:read')")
  public String getAdmin() {
    return "GET:: admin controller";
  }

  @PostMapping
  @PreAuthorize("hasAuthority('admin:create')")
  public String post() {
    return "CREATE:: admin controller";
  }

  @PutMapping
  @PreAuthorize("hasAuthority('admin:update')")
  public String put() {
    return "UPDATE:: admin controller";
  }
}
