package com.huylam.realestateserver.service;

import com.huylam.realestateserver.entity.District;
import com.huylam.realestateserver.entity.Property;
import com.huylam.realestateserver.entity.Province;
import com.huylam.realestateserver.repository.DistrictRepository;
import com.huylam.realestateserver.repository.PropertyRepository;
import com.huylam.realestateserver.repository.ProvinceRepository;
import com.huylam.realestateserver.service.DTO.PropertyDTO;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

  @Autowired
  PropertyRepository propertyRepository;

  private final ProvinceRepository provinceRepository;
  private final DistrictRepository districtRepository;

  public long countAllPropertiesService() {
    return propertyRepository.count();
  }

  public ArrayList<Property> getAllPropertiesService() {
    ArrayList<Property> listProperty = new ArrayList<Property>();
    propertyRepository.findAll().forEach(listProperty::add);
    return listProperty;
  }

  public Page<PropertyDTO> getAllPropertiesDTOService(Pageable pageable) {
    return propertyRepository.findAll(pageable).map(PropertyDTO::new);
  }

  public PropertyDTO getPropertyDTOByIdService(Long id) {
    Optional<Property> propertyData = propertyRepository.findById(id);
    if (propertyData.isPresent()) {
      Property property = propertyData.get();
      return new PropertyDTO(property);
    }
    return null; // or return an empty PropertyDTO if desired
  }

  public PropertyService(
    ProvinceRepository provinceRepository,
    DistrictRepository districtRepository,
    PropertyRepository propertyRepository
  ) {
    this.provinceRepository = provinceRepository;
    this.districtRepository = districtRepository;
    this.propertyRepository = propertyRepository;
  }

  public Property createProperty(
    int provinceId,
    int districtId,
    Property paramProperty
  ) {
    Optional<Province> propertyProvince = provinceRepository.findById(
      provinceId
    );
    Optional<District> propertyDistrict = districtRepository.findById(
      districtId
    );
    if (propertyDistrict.isPresent()) {
      Property newProperty = new Property();
      newProperty.setPropertyAddressNumber(
        paramProperty.getPropertyAddressNumber()
      );
      newProperty.setPropertyAddressStreet(
        paramProperty.getPropertyAddressStreet()
      );
      newProperty.setPropertyArea(paramProperty.getPropertyArea());
      newProperty.setPropertyDescription(
        paramProperty.getPropertyDescription()
      );
      newProperty.setPropertyFloorLocation(
        paramProperty.getPropertyFloorLocation()
      );
      newProperty.setPropertyFloorUnits(paramProperty.getPropertyFloorUnits());
      newProperty.setPropertyLandDirection(
        paramProperty.getPropertyLandDirection()
      );
      newProperty.setPropertyLandLegalStatus(
        paramProperty.getPropertyLandLegalStatus()
      );
      newProperty.setPropertyLandType(paramProperty.getPropertyLandType());
      newProperty.setPropertyLength(paramProperty.getPropertyLength());
      newProperty.setPropertyPostingStatus(
        paramProperty.getPropertyPostingStatus()
      );
      newProperty.setPropertyPrice(paramProperty.getPropertyPrice());
      newProperty.setPropertyWidth(paramProperty.getPropertyWidth());
      newProperty.setPropertyBedrooms(paramProperty.getPropertyBedrooms());
      newProperty.setPropertyBathrooms(paramProperty.getPropertyBathrooms());
      newProperty.setCreatedDate(LocalDateTime.now());

      Province _province = propertyProvince.get();
      District _district = propertyDistrict.get();
      newProperty.setProvince(_province);
      newProperty.setDistrict(_district);

      return propertyRepository.save(newProperty);
    }
    throw new EntityNotFoundException(
      "District not found with id " + districtId
    );
  }
}
