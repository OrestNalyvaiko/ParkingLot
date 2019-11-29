package com.nalyvaiko.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nalyvaiko.model.City;
import com.nalyvaiko.model.Country;
import com.nalyvaiko.model.DesiredReservation;
import com.nalyvaiko.model.ParkingLot;
import com.nalyvaiko.model.Region;
import com.nalyvaiko.model.Role;
import com.nalyvaiko.model.User;
import com.nalyvaiko.repository.DesiredReservationRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DesiredReservationServiceTest {

  private DesiredReservation desiredReservation;
  @Mock
  private DesiredReservationRepository desiredReservationRepository;
  @InjectMocks
  private DesiredReservationService desiredReservationService;

  @Before
  public void setup() {
    User user = new User();
    user.setName("Orest");
    user.setSurname("Nalyvaiko");
    user.setUsername("orest");
    user.setPassword("123");
    user.setEmail("orest@gmail.com");
    Role role = new Role();
    role.setRoleName("ROLE_USER");
    user.setRole(role);
    Country country = new Country();
    country.setCountryName("Ukraine");
    Region region = new Region();
    region.setRegionName("Lvivska");
    region.setCountry(country);
    City city = new City();
    city.setCityName("Lviv");
    city.setRegion(region);
    ParkingLot parkingLot = new ParkingLot();
    parkingLot.setId(1L);
    parkingLot.setLng(new BigDecimal(12.345));
    parkingLot.setLat(new BigDecimal(12.345));
    parkingLot.setTotalPlaces(20);
    parkingLot.setCostPerHour(new BigDecimal(25.0));
    parkingLot.setAddress("3 Kokorudzy");
    parkingLot.setCity(city);
    desiredReservation = new DesiredReservation();
    desiredReservation.setStartDate(
        Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 16, 30, 0)));
    desiredReservation.setEndDate(
        Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 17, 30, 0)));
    desiredReservation.setUser(user);
    desiredReservation.setParkingLot(parkingLot);
  }

  @Test
  public void whenMakeDesiredReservationThenReturnDesiredReservation() {
    when(desiredReservationRepository.save(desiredReservation))
        .thenReturn(desiredReservation);
    DesiredReservation savedDesiredReservation = desiredReservationService
        .makeDesiredReservation(desiredReservation);

    assertNotNull("Return null not saved desired reservation ",
        savedDesiredReservation);
    verify(desiredReservationRepository).save(desiredReservation);
  }
}
