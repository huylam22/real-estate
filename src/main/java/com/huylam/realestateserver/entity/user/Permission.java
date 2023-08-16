package com.huylam.realestateserver.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
  ADMIN_READ("admin:read"),
  ADMIN_UPDATE("admin:update"),
  ADMIN_CREATE("admin:create"),
  ADMIN_DELETE("admin:delete"),
  MANAGER_READ("manager:read"),
  MANAGER_UPDATE("manager:update"),
  MANAGER_CREATE("manager:create"),
  MANAGER_DELETE("manager:delete"),
  USER_READ("user:read"), // New permission
  USER_CREATE("user:create"), // New permission
  USER_UPDATE("user:update"), // New permission
  USER_DELETE("user:delete"); // New permission

  @Getter
  private final String permission;
}
