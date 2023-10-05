package com.huylam.realestateserver.repository;

import com.huylam.realestateserver.entity.Property;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PropertyRepository extends JpaRepository<Property, Long> {
  List<Property> findByPropertyLandType(String propertyLandType);
  Page<Property> findByPropertyLandType(
    String propertyLandType,
    Pageable pageable
  );

  @Query(
    "SELECT p FROM Property p WHERE LOWER(p.propertyPostingStatus) LIKE LOWER(concat('%', :status, '%'))"
  )
  Page<Property> findByPropertyPostingStatus(
    @Param("status") String propertyPostingStatus,
    Pageable pageable
  );

  long count();
}
