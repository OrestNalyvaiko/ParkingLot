package com.nalyvaiko.service;

import com.nalyvaiko.exception.SuchAddressExistException;
import com.nalyvaiko.model.City;
import com.nalyvaiko.model.Country;
import com.nalyvaiko.model.ParkingLot;
import com.nalyvaiko.model.Region;
import com.nalyvaiko.repository.CityRepository;
import com.nalyvaiko.repository.CountryRepository;
import com.nalyvaiko.repository.ParkingLotRepository;
import com.nalyvaiko.repository.RegionRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ParkingLotService {

  @Autowired
  private ParkingLotRepository parkingLotRepository;
  @Autowired
  private CountryRepository countryRepository;
  @Autowired
  private RegionRepository regionRepository;
  @Autowired
  private CityRepository cityRepository;
  private static Logger logger = LogManager.getLogger();

  public List<ParkingLot> getParkingLots() {
    logger.info("Find all parking lots from repository");
    return parkingLotRepository.findAll();
  }

  public ParkingLot addParkingLot(ParkingLot parkingLot)
      throws SuchAddressExistException {
    logger.info(
        "Check if parking lot with such address, city, region, country exist in DB");
    ParkingLot parkingLotExist = parkingLotRepository
        .findParkingLotByAddressCityRegionCountryNames(parkingLot.getAddress(),
            parkingLot.getCity().getCityName(),
            parkingLot.getCity().getRegion().getRegionName(),
            parkingLot.getCity().getRegion().getCountry().getCountryName());
    if (Objects.nonNull(parkingLotExist)) {
      logger.warn("SuchAddressExistException is thrown");
      throw new SuchAddressExistException();
    } else {
      checkIfCountryExist(parkingLot);
      ParkingLot savedParkingLot = parkingLotRepository.save(parkingLot);
      logger.info("ParkingLot is saved to DB");
      return savedParkingLot;
    }
  }

  private void checkIfCountryExist(ParkingLot parkingLot) {
    Country countryFromDB = countryRepository.findCountryByCountryName(
        parkingLot.getCity().getRegion().getCountry().getCountryName());
    if (Objects.nonNull(countryFromDB)) {
      parkingLot.getCity().getRegion().setCountry(countryFromDB);
      checkIfRegionExist(parkingLot);
    }
  }

  private void checkIfRegionExist(ParkingLot parkingLot) {
    Region regionFromDB = regionRepository
        .findRegionByRegionNameAndCountryId(
            parkingLot.getCity().getRegion().getRegionName(),
            parkingLot.getCity().getRegion().getCountry().getId());
    if (Objects.nonNull(regionFromDB)) {
      parkingLot.getCity().setRegion(regionFromDB);
      checkIfCityExist(parkingLot);
    }
  }

  private void checkIfCityExist(ParkingLot parkingLot) {
    City cityFromDB = cityRepository
        .findCityByCityNameAndRegionId(parkingLot.getCity().getCityName(),
            parkingLot.getCity().getRegion().getId());
    if (Objects.nonNull(cityFromDB)) {
      parkingLot.setCity(cityFromDB);
    }
  }

  public void updateParkingLotPlacesAndCostPerHour(Long id,
      Integer totalPlaces, BigDecimal costPerHour) {
    Optional.ofNullable(costPerHour).filter(c -> c.scale() <= 2)
        .orElseThrow(RuntimeException::new);
    parkingLotRepository
        .updateParkingLotPlacesAndCostPerHour(id, costPerHour, totalPlaces);
    logger
        .info(
            "Total places and cost per hour in parking lot with id = " + id
                + " are updated");
  }
}
