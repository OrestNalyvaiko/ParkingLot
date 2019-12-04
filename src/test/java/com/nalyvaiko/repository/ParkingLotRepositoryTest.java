package com.nalyvaiko.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.nalyvaiko.model.City;
import com.nalyvaiko.model.Country;
import com.nalyvaiko.model.ParkingLot;
import com.nalyvaiko.model.Region;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class ParkingLotRepositoryTest {

  private ParkingLot parkingLot;
  @Autowired
  private ParkingLotRepository parkingLotRepository;
  @Autowired
  private TestEntityManager entityManager;

  @Before
  public void setup() {
    Country country = new Country();
    country.setCountryName("Ukraine");
    Region region = new Region();
    region.setRegionName("Lvivska");
    region.setCountry(country);
    City city = new City();
    city.setCityName("Lviv");
    city.setRegion(region);
    parkingLot = new ParkingLot();
    parkingLot.setLng(new BigDecimal(12.345));
    parkingLot.setLat(new BigDecimal(12.345));
    parkingLot.setTotalPlaces(20);
    parkingLot.setCostPerHour(new BigDecimal(23.5));
    parkingLot.setAddress("3 Kokorudzy");
    parkingLot.setCity(city);
    entityManager.persistAndFlush(parkingLot);
  }

  @Test
  public void whenFindParkingLotByAddressCountryCityRegionNamesThenReturnParkingLot() {
    ParkingLot parkingLotFromDB = parkingLotRepository
        .findParkingLotByAddressCityRegionCountryNames(
            this.parkingLot.getAddress(),
            this.parkingLot.getCity().getCityName(),
            this.parkingLot.getCity().getRegion().getRegionName(),
            this.parkingLot.getCity().getRegion().getCountry()
                .getCountryName());

    assertNotNull("Parking lot from DB is null ", parkingLotFromDB);
  }

  @Test
  public void whenUpdateParkingLotPlacesAndCostPerHourThenUpdate() {
    parkingLotRepository
        .updateParkingLotPlacesAndCostPerHour(parkingLot.getId(),
            BigDecimal.valueOf(100, 2), 100);
    parkingLot = parkingLotRepository.findById(parkingLot.getId())
        .orElseThrow(RuntimeException::new);

    assertEquals("Total places are not equal ", 100,
        parkingLot.getTotalPlaces());
    assertEquals("Cost per hour are not equal ", BigDecimal.valueOf(100, 2),
        parkingLot.getCostPerHour());
  }
}