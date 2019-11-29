package com.nalyvaiko.repository;

import static org.junit.Assert.assertTrue;

import com.nalyvaiko.model.City;
import com.nalyvaiko.model.Country;
import com.nalyvaiko.model.DesiredReservation;
import com.nalyvaiko.model.ParkingLot;
import com.nalyvaiko.model.Region;
import com.nalyvaiko.model.Role;
import com.nalyvaiko.model.User;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
public class DesiredReservationRepositoryTest {

  private DesiredReservation desiredReservation;
  private ParkingLot parkingLot;
  private User user;
  @Autowired
  private DesiredReservationRepository desiredReservationRepository;
  @Autowired
  private TestEntityManager entityManager;

  @Before
  public void setup() {
    user = new User();
    user.setName("Orest");
    user.setSurname("Nalyvaiko");
    user.setUsername("orest");
    user.setPassword("123");
    user.setEmail("orest@gmail.com");
    Role role = new Role();
    role.setRoleName("ROLE_USER");
    entityManager.persistAndFlush(role);
    user.setRole(role);
    entityManager.persistAndFlush(user);
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
    parkingLot.setCostPerHour(new BigDecimal(25.0));
    parkingLot.setAddress("3 Kokorudzy");
    parkingLot.setCity(city);
    entityManager.persistAndFlush(parkingLot);
    desiredReservation = new DesiredReservation();
    desiredReservation.setUser(user);
    desiredReservation.setStartDate(
        Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 16, 30, 0)));
    desiredReservation.setEndDate(
        Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 17, 30, 0)));
    desiredReservation.setParkingLot(parkingLot);
    entityManager.persistAndFlush(desiredReservation);
  }

  @Test
  public void whenFindDesiredReservationsWithMatchDatesThenReturnOptionalListOfDesiredReservations() {
    Optional<List<DesiredReservation>> desiredReservationsFromDB = desiredReservationRepository
        .findDesiredReservationsWhichMatchDates(
            Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 15, 30, 0)),
            Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 17, 30, 0)));

    assertTrue("Desired reservations from DB does not match dates",
        desiredReservationsFromDB.isPresent());
  }
}
