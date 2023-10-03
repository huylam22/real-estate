package com.huylam.realestateserver.repository;

import com.huylam.realestateserver.entity.Property;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
  List<Property> findByPropertyLandType(String propertyLandType);
  Page<Property> findByPropertyLandType(
    String propertyLandType,
    Pageable pageable
  );
  Page<Property> findByPropertyPostingStatus(
    String propertyPostingStatus,
    Pageable pageable
  );
  long count();
}
