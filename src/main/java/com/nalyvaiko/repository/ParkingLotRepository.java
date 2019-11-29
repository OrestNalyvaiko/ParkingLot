package com.nalyvaiko.repository;

import com.nalyvaiko.model.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {

  @Query(value = "SELECT p FROM ParkingLot p "
      + "INNER JOIN City ct ON p.city = ct.id "
      + "INNER JOIN Region r ON ct.region = r.id "
      + "INNER JOIN Country co ON r.country = co.id "
      + "WHERE p.address = :address "
      + "AND ct.cityName = :cityName "
      + "AND r.regionName = :regionName "
      + "AND co.countryName = :countryName ")
  ParkingLot findParkingLotByAddressCityRegionCountryNames(String address,
      String cityName,
      String regionName, String countryName);
}
