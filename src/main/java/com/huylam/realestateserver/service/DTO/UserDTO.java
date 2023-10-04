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

  private Long id;
  private String username;
  private String email;
  private String firstname;
  private String lastname;
  private String avatar_url;

  @Autowired
  public UserDTO(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.firstname = user.getFirstname();
    this.lastname = user.getLastname();
    this.avatar_url = user.getAvatar_url();
  }
}
