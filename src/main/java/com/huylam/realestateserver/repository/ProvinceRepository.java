package com.huylam.realestateserver.repository;

import com.huylam.realestateserver.entity.Province;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProvinceRepository extends JpaRepository<Province, Integer> {
  @Query("SELECT DISTINCT p FROM Province p LEFT JOIN FETCH p.districts")
  List<Province> findAllWithDistricts();
}
