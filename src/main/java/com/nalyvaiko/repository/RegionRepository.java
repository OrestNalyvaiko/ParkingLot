package com.nalyvaiko.repository;

import com.nalyvaiko.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

  @Query("SELECT r FROM Region r "
      + "WHERE r.regionName = :regionName AND "
      + "r.country.id = :countryId")
  Region findRegionByRegionNameAndCountryId(String regionName, Long countryId);
}
