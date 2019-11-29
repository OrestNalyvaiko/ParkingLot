package com.nalyvaiko.repository;

import com.nalyvaiko.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

  @Query("SELECT c FROM City c "
      + "WHERE c.cityName = :cityName AND "
      + "c.region.id = :regionId")
  City findCityByCityNameAndRegionId(String cityName, Long regionId);
}
