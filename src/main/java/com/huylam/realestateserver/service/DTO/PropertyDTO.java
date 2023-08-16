package com.huylam.realestateserver.service.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.huylam.realestateserver.entity.Property;
// import com.huylam.realestateserver.entity.user.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDTO {

  private long id;
  private String propertyAddressNumber;
  private String propertyAddressStreet;
  private float propertyArea;
  private float propertyWidth;
  private float propertyLength;
  private int propertyFloorUnits;
  private String propertyFloorLocation;
  private float propertyPrice;
  private String propertyLandType;
  private String propertyLandDirection;
  private String propertyLandLegalStatus;
  private String propertyDescription;
  private String propertyPostingStatus;
  private int propertyBedrooms;
  private int propertyBathrooms;
  private int provinceId;
  private int districtId;
  private String provincePrefix;
  private String districtPrefix;
  private String districtName;
  private String provinceName;
  private List<String> propertyCoverPaths;
  private UserDTO user;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
  private LocalDateTime createdDate;

  @Autowired
  public PropertyDTO(Property property) {
    this.id = property.getId();
    this.propertyAddressNumber = property.getPropertyAddressNumber();
    this.propertyAddressStreet = property.getPropertyAddressStreet();
    this.propertyArea = property.getPropertyArea();
    this.propertyWidth = property.getPropertyWidth();
    this.propertyLength = property.getPropertyLength();
    this.propertyFloorUnits = property.getPropertyFloorUnits();
    this.propertyFloorLocation = property.getPropertyFloorLocation();
    this.propertyPrice = property.getPropertyPrice();
    this.propertyLandType = property.getPropertyLandType();
    this.propertyLandDirection = property.getPropertyLandDirection();
    this.propertyLandLegalStatus = property.getPropertyLandLegalStatus();
    this.propertyDescription = property.getPropertyDescription();
    this.propertyPostingStatus = property.getPropertyPostingStatus();
    this.provinceId = property.getProvince().getId();
    this.districtId = property.getDistrict().getId();
    this.provincePrefix = property.getProvince().getProvinceCode();
    this.provinceName = property.getProvince().getProvinceName();
    this.districtPrefix = property.getDistrict().getDistrictPrefix();
    this.districtName = property.getDistrict().getDistrictName();
    this.createdDate = property.getCreatedDate();
    this.propertyBathrooms = property.getPropertyBathrooms();
    this.propertyBedrooms = property.getPropertyBedrooms();
    this.propertyCoverPaths = property.getPropertyCoverPaths();
    this.user = new UserDTO(property.getUser());
  }
}
