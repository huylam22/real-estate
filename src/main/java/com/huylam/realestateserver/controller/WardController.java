package com.huylam.realestateserver.controller;

import com.huylam.realestateserver.entity.Ward;
import com.huylam.realestateserver.repository.DistrictRepository;
import com.huylam.realestateserver.repository.WardRepository;
import com.huylam.realestateserver.service.WardService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("api/v1/wards")
public class WardController {

  @Autowired
  WardRepository pWardRepository;

  @Autowired
  DistrictRepository pDistrictRepository;

  @Autowired
  WardService pWardService;

  @GetMapping
  public ResponseEntity<List<Ward>> getAllWards() {
    try {
      return new ResponseEntity<>(
        pWardService.getAllWardsService(),
        HttpStatus.OK
      );
    } catch (Exception e) {
      // TODO: handle exception
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
