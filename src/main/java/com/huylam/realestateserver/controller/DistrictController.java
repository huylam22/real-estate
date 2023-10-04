package com.huylam.realestateserver.controller;

import com.huylam.realestateserver.entity.District;
import com.huylam.realestateserver.entity.Province;
import com.huylam.realestateserver.repository.DistrictRepository;
import com.huylam.realestateserver.repository.ProvinceRepository;
import com.huylam.realestateserver.service.DistrictService;
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
@RequestMapping("api/v1/districts")
public class DistrictController {

  @Autowired
  DistrictRepository districtRepository;

  @Autowired
  DistrictService districtService;

  @Autowired
  ProvinceRepository provinceRepository;

  // Lấy all district dùng service
  @GetMapping
  public ResponseEntity<List<District>> getAllDistricts() {
    try {
      return new ResponseEntity<>(
        districtService.getDistrictList(),
        HttpStatus.OK
      );
    } catch (Exception e) {
      // TODO: handle exception
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Lấy district theo {id} dùng service
  @GetMapping("/details/{id}") // Dùng phương thức GET
  public ResponseEntity<District> getDistrictById(@PathVariable("id") int id) {
    try {
      Optional<District> districtData = districtRepository.findById(id);
      if (districtData.isPresent()) {
        return new ResponseEntity<>(districtData.get(), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      // TODO: handle exception
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/details/province/{provinceId}")
  public ResponseEntity<List<District>> getDistrictsByProvinceId(
    @PathVariable("provinceId") int provinceId
  ) {
    try {
      List<District> districts = districtRepository.findByProvinceId(
        provinceId
      );
      if (!districts.isEmpty()) {
        return new ResponseEntity<>(districts, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      // Handle exceptions appropriately, e.g., log the error
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Tạo MỚI District sử dụng phương thức POST
  //  @CrossOrigin
  @PostMapping("/create/{id}") // Dùng phương thức POST
  public ResponseEntity<Object> createCDistrict(
    @PathVariable("id") int id,
    @RequestBody District district
  ) {
    try {
      Optional<Province> provinceData = provinceRepository.findById(id);
      if (provinceData.isPresent()) {
        District newDistrict = new District();
        newDistrict.setDistrictName(district.getDistrictName());
        newDistrict.setDistrictPrefix(district.getDistrictPrefix());
        newDistrict.setId(district.getId());

        Province _province = provinceData.get();
        newDistrict.setProvince(_province);

        District savedDistrict = districtRepository.save(newDistrict);
        return new ResponseEntity<>(savedDistrict, HttpStatus.CREATED);
      }
    } catch (Exception e) {
      System.out.println(
        "+++++++++++++++++++++::::: " + e.getCause().getCause().getMessage()
      );
      //Hiện thông báo lỗi tra back-end
      //return new ResponseEntity<>(e.getCause().getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      return ResponseEntity
        .unprocessableEntity()
        .body(
          "Failed to Create specified District: " +
          e.getCause().getCause().getMessage()
        );
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  // Sửa/update voucher theo {id} KHÔNG dùng service, sử dụng phương thức PUT
  @PutMapping("/update/{id}") // Dùng phương thức PUT
  public ResponseEntity<Object> updateCDistrictById(
    @PathVariable("id") int id,
    @RequestBody District district
  ) {
    try {
      Optional<District> districtData = districtRepository.findById(id);
      if (districtData.isPresent()) {
        District newDistrict = districtData.get();

        newDistrict.setDistrictName(district.getDistrictName());

        newDistrict.setDistrictPrefix(district.getDistrictPrefix());

        District _district = districtRepository.save(newDistrict);
        return new ResponseEntity<>(_district, HttpStatus.OK);
      } else {
        return ResponseEntity
          .badRequest()
          .body("Failed to get specified Voucher: " + id + "  for update.");
      }
    } catch (Exception e) {
      return ResponseEntity
        .unprocessableEntity()
        .body(
          "Failed to Update specified Voucher:" +
          e.getCause().getCause().getMessage()
        );
    }
  }

  // Xoá/delete district theo {id} KHÔNG dùng service, sử dụng phương thức DELETE
  @DeleteMapping("/delete/{id}") // Dùng phương thức DELETE
  public ResponseEntity<District> deleteCDistrictById(
    @PathVariable("id") int id
  ) {
    try {
      Optional<District> districtData = districtRepository.findById(id);
      if (districtData.isPresent()) {
        districtRepository.deleteById(id);
      }
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
