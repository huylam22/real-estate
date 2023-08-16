package com.huylam.realestateserver.service;

import com.huylam.realestateserver.entity.Ward;
import com.huylam.realestateserver.repository.WardRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WardService {

  @Autowired
  WardRepository wardRepository;

  public ArrayList<Ward> getAllWardsService() {
    ArrayList<Ward> Wards = new ArrayList<>();
    wardRepository.findAll().forEach(Wards::add);
    return Wards;
  }
}
