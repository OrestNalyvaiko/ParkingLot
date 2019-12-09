package com.nalyvaiko.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.nalyvaiko.model.City;
import com.nalyvaiko.model.Country;
import com.nalyvaiko.model.ParkingLot;
import com.nalyvaiko.model.PlaceStatus;
import com.nalyvaiko.model.Region;
import com.nalyvaiko.model.Reservation;
import com.nalyvaiko.model.Role;
import com.nalyvaiko.model.User;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class ReservationRepositoryTest {

  private Reservation reservation;
  private ParkingLot parkingLot;
  private User user;
  private PlaceStatus placeStatus;
  @Autowired
  private ReservationRepository reservationRepository;
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
    placeStatus = new PlaceStatus();
    placeStatus.setStatus("reserved");
    entityManager.persistAndFlush(placeStatus);
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
    reservation = new Reservation();
    reservation.setPlaceStatus(placeStatus);
    reservation.setPlace(1);
    reservation.setUser(user);
    reservation.setStartDate(
        Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 16, 30, 0)));
    reservation.setEndDate(
        Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 17, 30, 0)));
    reservation.setPrice(new BigDecimal(25.0));
    reservation.setParkingLot(parkingLot);
    entityManager.persistAndFlush(reservation);
  }

  @Test
  public void whenFindReservationsByParkingLotIdAndTimeThenReturnListOfReservations() {
    List<Reservation> reservations = new ArrayList<>();
    reservations.add(reservation);

    List<Reservation> reservationsFromDB = reservationRepository
        .findReservationsByParkingLotIdAndTime(parkingLot.getId(),
            Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 17, 30, 0)));

    assertNotNull("Reservations from DB is null ", reservationsFromDB);
    assertEquals("Expected reservations and reservation from DB are not equal ",
        reservations, reservationsFromDB);
  }

  @Test
  public void whenFindReservationByUsernameAndTimeThenReturnListOfReservations() {
    Reservation reservation = new Reservation();
    reservation.setPlaceStatus(placeStatus);
    reservation.setPlace(3);
    reservation.setUser(user);
    reservation.setStartDate(
        Timestamp.valueOf(LocalDateTime.of(2019, 11, 12, 8, 30, 0)));
    reservation.setEndDate(
        Timestamp.valueOf(LocalDateTime.of(2019, 11, 12, 9, 30, 0)));
    reservation.setPrice(new BigDecimal(25.0));
    reservation.setParkingLot(parkingLot);
    entityManager.persistAndFlush(reservation);

    List<Reservation> reservations = new ArrayList<>();
    reservations.add(this.reservation);
    reservations.add(reservation);

    List<Reservation> reservationsFromDB = reservationRepository
        .findReservationsByUsernameAndTime(user.getUsername(),
            Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 17, 30, 0)));

    assertNotNull("Reservations from DB is null ", reservationsFromDB);
    assertEquals("Expected reservations and reservation from DB are not equal ",
        reservations, reservationsFromDB);
  }

  @Test
  public void whenFindReservationsByParkingLotIdStartAndEndDatesReturnOptionalListOfReservations() {
    List<Reservation> reservations = new ArrayList<>();
    reservations.add(reservation);

    Optional<List<Reservation>> reservationList = reservationRepository
        .findReservationsByParkingLotIdStartAndEndDates(
            reservation.getParkingLot().getId(),
            Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 16, 30, 0)),
            Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 17, 30, 0)));

    assertNotNull("Reservations from DB is null", reservationList);
    assertEquals(
        "Expected reservations and reservations from DB are not equal ",
        reservations, reservationList.orElseThrow(RuntimeException::new));
  }
}
