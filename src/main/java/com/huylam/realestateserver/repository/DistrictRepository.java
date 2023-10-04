package com.huylam.realestateserver.repository;

import com.huylam.realestateserver.entity.District;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistrictRepository extends JpaRepository<District, Integer> {
  List<District> findByProvinceId(int provinceId);
}
