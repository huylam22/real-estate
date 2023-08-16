package com.huylam.realestateserver.service;

import com.huylam.realestateserver.entity.Province;
import com.huylam.realestateserver.repository.ProvinceRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProvinceService {

  @Autowired
  ProvinceRepository provinceRepository;

  public ArrayList<Province> getProvinceList() {
    ArrayList<Province> Provinces = new ArrayList<Province>();
    provinceRepository.findAll().forEach(Provinces::add);
    return Provinces;
  }
}
