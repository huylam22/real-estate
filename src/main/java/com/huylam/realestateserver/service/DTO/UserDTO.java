package com.huylam.realestateserver.service.DTO;

import com.huylam.realestateserver.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  private String username;
  private String email;
  private String firstName;
  private String lastName;
  private String avatar_url;

  @Autowired
  public UserDTO(User user) {
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.firstName = user.getFirstname();
    this.lastName = user.getLastname();
    this.avatar_url = user.getAvatar_url();
  }
}
