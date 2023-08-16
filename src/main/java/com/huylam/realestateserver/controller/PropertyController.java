package com.huylam.realestateserver.controller;

import com.huylam.realestateserver.entity.District;
import com.huylam.realestateserver.entity.Property;
import com.huylam.realestateserver.entity.Province;
import com.huylam.realestateserver.entity.user.User;
import com.huylam.realestateserver.repository.DistrictRepository;
import com.huylam.realestateserver.repository.PropertyRepository;
import com.huylam.realestateserver.repository.ProvinceRepository;
import com.huylam.realestateserver.repository.WardRepository;
import com.huylam.realestateserver.repository.auth.UserRepository;
import com.huylam.realestateserver.service.DTO.PropertyDTO;
import com.huylam.realestateserver.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class PropertyController {

  @Autowired
  PropertyRepository propertyRepository;

  @Autowired
  PropertyService propertyService;

  @Autowired
  ProvinceRepository provinceRepository;

  @Autowired
  DistrictRepository districtRepository;

  @Autowired
  WardRepository wardRepository;

  @Autowired
  UserRepository userRepository;

  @Operation(
    summary = "Get all properties",
    description = "Returns all properties with specified DTO format",
    tags = { "property-controller" }
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = @Content(schema = @Schema(implementation = Property.class))
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Error",
        content = @Content
      ),
    }
  )
  @GetMapping({ "/properties" })
  public ResponseEntity<List<PropertyDTO>> getAllProperties() {
    try {
      List<PropertyDTO> propertyDTOs =
        propertyService.getAllPropertiesDTOService();
      return new ResponseEntity<>(propertyDTOs, HttpStatus.OK);
    } catch (Exception e) {
      // TODO: handle exception
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/properties/count")
  public long countAllRealEstates() {
    return propertyService.countAllPropertiesService();
  }

  @Operation(
    summary = "Find Property by ID",
    description = "Returns a single property",
    tags = { "property-controller" }
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "successful operation",
        content = @Content(schema = @Schema(implementation = Property.class))
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Invalid ID supplied",
        content = @Content
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Property not found",
        content = @Content
      ),
    }
  )
  @GetMapping("/properties/{propertyId}")
  public ResponseEntity<?> getProperty(
    @Parameter(description = "Id of property to be searched") @PathVariable(
      value = "propertyId",
      required = true
    ) long propertyId
  ) {
    Optional<Property> propertyData = propertyRepository.findById(propertyId);
    if (propertyData.isPresent()) {
      try {
        PropertyDTO property = propertyService.getPropertyDTOByIdService(
          propertyId
        ); // Implement this method in your service layer to get the property by id
        return ResponseEntity.ok(property);
      } catch (Exception e) {
        String errorMessage = "Something went wrong";
        return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(errorMessage);
      }
    }
    String errorMessage = "Property with id " + propertyId + " not found";
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
  }

  @Operation(
    summary = "Find Similar Land Type Property by ID",
    description = "Returns list of similar properties based on land type",
    tags = { "property-controller" }
  )
  @GetMapping("/properties/{propertyId}/similar")
  public ResponseEntity<List<PropertyDTO>> getSimilarProperties(
    @Parameter(description = "Id of property to be searched") @PathVariable(
      value = "propertyId",
      required = true
    ) long id
  ) {
    Optional<Property> propertyData = propertyRepository.findById(id);
    if (propertyData.isPresent()) {
      try {
        Property property = propertyData.get();
        String propertyLandType = property.getPropertyLandType();
        List<Property> similarProperties =
          propertyRepository.findByPropertyLandType(propertyLandType);
        List<PropertyDTO> similarPropertiesDTO = new ArrayList<>();
        for (Property prop : similarProperties) {
          PropertyDTO propDTO = new PropertyDTO(prop);
          similarPropertiesDTO.add(propDTO);
        }
        return new ResponseEntity<>(similarPropertiesDTO, HttpStatus.OK);
      } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Operation(
    summary = "Create Property",
    description = "Creates a new property",
    tags = { "property-controller" }
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "201",
        description = "Property created successfully",
        content = @Content(schema = @Schema(implementation = Property.class))
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Invalid ID supplied",
        content = @Content
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Province, District, or User not found",
        content = @Content
      ),
      @ApiResponse(
        responseCode = "422",
        description = "Failed to create Property",
        content = @Content
      ),
    }
  )
  @PostMapping("/properties/create/{provinceId}/{districtId}/{userId}")
  public ResponseEntity<Object> createProperty(
    @Parameter(description = "District ID") @PathVariable(
      "districtId"
    ) int districtId,
    @Parameter(description = "Province ID") @PathVariable(
      "provinceId"
    ) int provinceId,
    @Parameter(description = "User ID") @PathVariable("userId") Long userId,
    @RequestBody Property paramProperty
  ) {
    try {
      Optional<Province> propertyProvince = provinceRepository.findById(
        provinceId
      );
      Optional<District> propertyDistrict = districtRepository.findById(
        districtId
      );
      Optional<User> propertyUser = userRepository.findById(userId);
      if (propertyDistrict.isPresent() && propertyUser.isPresent()) {
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
        newProperty.setPropertyFloorUnits(
          paramProperty.getPropertyFloorUnits()
        );
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
        User _user = propertyUser.get();
        newProperty.setProvince(_province);
        newProperty.setDistrict(_district);
        newProperty.setUser(_user);
        Property savedProperty = propertyRepository.save(newProperty);
        return new ResponseEntity<>(savedProperty, HttpStatus.CREATED);
      }
    } catch (Exception e) {
      // TODO: handle exception
      System.out.println(
        "+++++++++++++++++++++::::: " + e.getCause().getCause().getMessage()
      );
      //Hiện thông báo lỗi tra back-end
      //return new ResponseEntity<>(e.getCause().getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      return ResponseEntity
        .unprocessableEntity()
        .body(
          "Failed to Create specified Property: " +
          e.getCause().getCause().getMessage()
        );
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @Operation(
    summary = "Update Property",
    description = "Updates an existing property",
    tags = { "property-controller" }
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "200",
        description = "Property updated successfully",
        content = @Content(schema = @Schema(implementation = Property.class))
      ),
      @ApiResponse(
        responseCode = "404",
        description = "Property not found",
        content = @Content
      ),
    }
  )
  @CrossOrigin
  @PutMapping("/update/{id}")
  public ResponseEntity<Object> updateProperty(
    @PathVariable("id") Long id,
    @RequestBody Property paramProperty
  ) {
    Optional<Property> propertyData = propertyRepository.findById(id);
    if (propertyData.isPresent()) {
      Property newProperty = propertyData.get();
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
      Property savedEmployee = propertyRepository.save(newProperty);
      return new ResponseEntity<>(savedEmployee, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Operation(
    summary = "Delete Property",
    description = "Deletes a property by ID",
    tags = { "property-controller" }
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "204",
        description = "Property deleted successfully"
      ),
      @ApiResponse(responseCode = "500", description = "Internal server error"),
    }
  )
  @CrossOrigin
  @DeleteMapping("/delete/{propertyId}")
  public ResponseEntity<Object> deleteProperty(
    @PathVariable("propertyId") Long propertyId
  ) {
    try {
      propertyRepository.deleteById(propertyId);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @Operation(
    summary = "Delete All Properties",
    description = "Deletes all properties",
    tags = { "property-controller" }
  )
  @ApiResponses(
    value = {
      @ApiResponse(
        responseCode = "204",
        description = "All properties deleted successfully"
      ),
      @ApiResponse(responseCode = "500", description = "Internal server error"),
    }
  )
  @DeleteMapping("/admin/delete-all")
  public ResponseEntity<Property> deleteAllProperties() {
    try {
      propertyRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
// @GetMapping("/properties/{propertyId}") //// OLD ERROR WITH NOT FOUND PROPERTY (Not showing not found message)
// public ResponseEntity<PropertyDTO> getProperty(
//   @PathVariable(value = "propertyId", required = true) long propertyId
// ) {
//   PropertyDTO property = propertyService.getPropertyDTOByIdService(
//     propertyId
//   ); // implement this method in your service layer to get the property by id
//   if (property == null) {
//     return ResponseEntity.notFound().build();
//   } else {
//     return ResponseEntity.ok(property);
//   }
// }
// @GetMapping("/properties/details/{propertyId}")
// public ResponseEntity<Object> getPropertyById(
//   @PathVariable(value = "propertyId", required = true) long id
// ) {
//   Optional<Property> propertyData = propertyRepository.findById(id);
//   if (propertyData.isPresent()) {
//     try {
//       Property property = propertyData.get();
//       return new ResponseEntity<>(property, HttpStatus.OK);
//     } catch (Exception e) {
//       return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//     }
//   } else {
//     // Property propertyNull = new Property();
//     return new ResponseEntity<>(
//       "Property with id " + id + " not found",
//       HttpStatus.NOT_FOUND
//     );
//   }
// }
