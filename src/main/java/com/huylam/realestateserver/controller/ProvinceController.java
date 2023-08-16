package com.huylam.realestateserver.controller;

import com.huylam.realestateserver.entity.Province;
import com.huylam.realestateserver.repository.ProvinceRepository;
import com.huylam.realestateserver.service.ProvinceService;
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
@RequestMapping("api/v1/provinces")
public class ProvinceController {

  @Autowired
  ProvinceRepository provinceRepository;

  @Autowired
  ProvinceService provinceService;

  // Lấy all province dùng service
  @GetMapping
  public ResponseEntity<List<Province>> getAllProvincesByService() {
    try {
      return new ResponseEntity<>(
        provinceService.getProvinceList(),
        HttpStatus.OK
      );
    } catch (Exception e) {
      // TODO: handle exception
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Lấy province theo {id} dùng service
  @GetMapping("/details/{id}") // Dùng phương thức GET
  public ResponseEntity<Province> getProvincesById(@PathVariable("id") int id) {
    try {
      Optional<Province> provinceData = provinceRepository.findById(id);
      if (provinceData.isPresent()) {
        return new ResponseEntity<>(provinceData.get(), HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      // TODO: handle exception
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Tạo MỚI province sử dụng phương thức POST
  @PostMapping("/create") // Dùng phương thức POST
  public ResponseEntity<Object> createCProvince(
    @RequestBody Province province
  ) {
    try {
      Optional<Province> provinceData = provinceRepository.findById(
        province.getId()
      );
      if (provinceData.isPresent()) {
        return ResponseEntity
          .unprocessableEntity()
          .body(" Province already exsit  ");
      }
      Province _provinces = provinceRepository.save(province);
      return new ResponseEntity<>(_provinces, HttpStatus.CREATED);
    } catch (Exception e) {
      System.out.println(
        "+++++++++++++++++++++::::: " + e.getCause().getCause().getMessage()
      );
      //Hiện thông báo lỗi tra back-end
      //return new ResponseEntity<>(e.getCause().getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      return ResponseEntity
        .unprocessableEntity()
        .body(
          "Failed to Create specified Province: " +
          e.getCause().getCause().getMessage()
        );
    }
  }

  // Sửa/update province theo {id} KHÔNG dùng service, sử dụng phương thức PUT
  @PutMapping("/update/{id}") // Dùng phương thức PUT
  public ResponseEntity<Object> updateCProvinceById(
    @PathVariable("id") int id,
    @RequestBody Province province
  ) {
    try {
      Optional<Province> provinceData = provinceRepository.findById(id);
      if (provinceData.isPresent()) {
        Province newProvince = provinceData.get();
        // if (province.getProvinceCode() != null && !province.getProvinceCode().isEmpty()) {
        newProvince.setProvinceCode(province.getProvinceCode());
        // }
        // if (province.getProvinceName() != null && !province.getProvinceName().isEmpty()) {
        newProvince.setProvinceName(province.getProvinceName());
        // }
        Province _province = provinceRepository.save(newProvince);
        return new ResponseEntity<>(_province, HttpStatus.OK);
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

  // Xoá/delete province theo {id} KHÔNG dùng service, sử dụng phương thức DELETE
  @DeleteMapping("/delete/{id}") // Dùng phương thức DELETE
  public ResponseEntity<Province> deleteCProvinceById(
    @PathVariable("id") int id
  ) {
    try {
      Optional<Province> provinceData = provinceRepository.findById(id);
      if (provinceData.isPresent()) {
        provinceRepository.deleteById(id);
      }
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
