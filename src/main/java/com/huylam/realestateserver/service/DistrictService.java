package com.huylam.realestateserver.service;

import com.huylam.realestateserver.entity.District;
import com.huylam.realestateserver.repository.DistrictRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistrictService {

  @Autowired
  DistrictRepository districtRepository;

  public ArrayList<District> getDistrictList() {
    ArrayList<District> Districts = new ArrayList<District>();
    districtRepository.findAll().forEach(Districts::add);
    return Districts;
  }
}
