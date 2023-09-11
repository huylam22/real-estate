package com.huylam.realestateserver.entity.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.huylam.realestateserver.entity.Property;
import com.huylam.realestateserver.entity.token.Token;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "first_name")
  @NotEmpty(message = "Please provide your first name")
  private String firstname;

  @Column(name = "last_name")
  @NotEmpty(message = "Please provide your last name")
  private String lastname;

  @Column(name = "email")
  @Email(message = "Please provide a valid Email")
  @NotEmpty(message = "Please provide an email")
  private String email;

  @Column(name = "password")
  @ValidPassword
  private String password;

  @Column(name = "avatar_url")
  private String avatar_url;

  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(mappedBy = "user")
  private List<Token> tokens;

  @OneToMany(
    targetEntity = Property.class,
    mappedBy = "user",
    cascade = CascadeType.ALL
  )
  @JsonManagedReference(value = "user-property")
  private Set<Property> properties;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
