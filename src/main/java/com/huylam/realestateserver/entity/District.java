package com.huylam.realestateserver.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "districts")
public class District {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "name")
  private String districtName;

  @Column(name = "prefix")
  private String districtPrefix;

  @ManyToOne
  @JsonBackReference(value = "province-district")
  @JoinColumn(name = "province_id")
  private Province province;

  @OneToMany(
    targetEntity = Property.class,
    mappedBy = "district",
    cascade = CascadeType.ALL
  )
  @JsonManagedReference(value = "district-property")
  // @JsonIgnore
  private Set<Property> properties;

  @OneToMany(
    targetEntity = Ward.class,
    mappedBy = "district",
    cascade = CascadeType.ALL
  )
  @JsonManagedReference(value = "district-ward")
  private Set<Ward> wards;
}
