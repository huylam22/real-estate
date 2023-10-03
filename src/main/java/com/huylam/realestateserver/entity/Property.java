package com.huylam.realestateserver.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huylam.realestateserver.entity.user.User;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "properties")
public class Property {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "property_address_number")
  private String propertyAddressNumber;

  @Column(name = "property_address_street")
  private String propertyAddressStreet;

  @Column(name = "property_area")
  private float propertyArea;

  @Column(name = "property_width")
  private float propertyWidth;

  @Column(name = "property_length")
  private int propertyLength;

  @Column(name = "property_floor_units")
  private int propertyFloorUnits;

  @Column(name = "property_floor_location")
  private String propertyFloorLocation;

  @Column(name = "property_bedrooms")
  private int propertyBedrooms;

  @Column(name = "property_bathrooms")
  private int propertyBathrooms;

  @Column(name = "property_price")
  private float propertyPrice;

  @Column(name = "property_land_type")
  private String propertyLandType;

  @Column(name = "property_land_direction")
  private String propertyLandDirection;

  @Column(name = "property_land_legal_status")
  private String propertyLandLegalStatus;

  @Column(name = "property_description")
  private String propertyDescription;

  @Column(name = "property_posting_status")
  private String propertyPostingStatus;

  @ElementCollection
  @CollectionTable(
    name = "property_cover_paths",
    joinColumns = @JoinColumn(name = "property_id")
  )
  @Column(name = "cover_path")
  private List<String> propertyCoverPaths = new ArrayList<>();

  @Column(name = "created_date")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  private LocalDateTime updatedDate;

  @ManyToOne
  @JoinColumn(name = "province_id")
  @JsonBackReference(value = "province-property")
  private Province province;

  @ManyToOne
  @JoinColumn(name = "district_id")
  @JsonBackReference(value = "district-property")
  private District district;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @JsonBackReference(value = "user-property")
  private User user;
}
