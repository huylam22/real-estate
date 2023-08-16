package com.huylam.realestateserver.repository;

import com.huylam.realestateserver.entity.Property;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
  List<Property> findByPropertyLandType(String propertyLandType);

  long count();
}
