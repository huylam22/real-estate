package com.huylam.realestateserver.entity;

// import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "provinces")
public class Province {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "code")
  private String provinceCode;

  @Column(name = "name")
  private String provinceName;

  @OneToMany(
    targetEntity = District.class,
    mappedBy = "province",
    cascade = CascadeType.ALL
  )
  @JsonManagedReference(value = "province-district")
  // @JsonIgnore
  private Set<District> districts;

  @OneToMany(
    targetEntity = Property.class,
    mappedBy = "province",
    cascade = CascadeType.ALL
  )
  @JsonManagedReference(value = "province-property")
  //   @JsonIgnore
  private Set<Property> properties;
}
