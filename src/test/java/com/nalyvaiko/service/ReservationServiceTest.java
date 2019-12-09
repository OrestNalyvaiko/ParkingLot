package com.nalyvaiko.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nalyvaiko.dto.StatisticDTO;
import com.nalyvaiko.mail.MailSender;
import com.nalyvaiko.model.City;
import com.nalyvaiko.model.Country;
import com.nalyvaiko.model.DesiredReservation;
import com.nalyvaiko.model.ParkingLot;
import com.nalyvaiko.model.PlaceStatus;
import com.nalyvaiko.model.Region;
import com.nalyvaiko.model.Reservation;
import com.nalyvaiko.model.Role;
import com.nalyvaiko.model.User;
import com.nalyvaiko.repository.DesiredReservationRepository;
import com.nalyvaiko.repository.PlaceStatusRepository;
import com.nalyvaiko.repository.ReservationRepository;
import com.nalyvaiko.repository.UserRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {

  private User user;
  private PlaceStatus placeStatus;
  private Reservation reservation;
  private ParkingLot parkingLot;
  private Clock fixedClock;
  @Mock
  private ReservationRepository reservationRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private PlaceStatusRepository placeStatusRepository;
  @Mock
  private Clock clock;
  @Mock
  private DesiredReservationRepository desiredReservationRepository;
  @Mock
  private MailSender mailSender;
  @InjectMocks
  private ReservationService reservationService;

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
    user.setRole(role);
    placeStatus = new PlaceStatus();
    placeStatus.setStatus("reserved");
    Country country = new Country();
    country.setCountryName("Ukraine");
    Region region = new Region();
    region.setRegionName("Lvivska");
    region.setCountry(country);
    City city = new City();
    city.setCityName("Lviv");
    city.setRegion(region);
    parkingLot = new ParkingLot();
    parkingLot.setId(1L);
    parkingLot.setLng(new BigDecimal(12.345));
    parkingLot.setLat(new BigDecimal(12.345));
    parkingLot.setTotalPlaces(20);
    parkingLot.setCostPerHour(new BigDecimal(25.0));
    parkingLot.setAddress("3 Kokorudzy");
    parkingLot.setCity(city);
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

    fixedClock = Clock
        .fixed(Instant.parse("2019-11-11T17:30:00.000Z"), ZoneId.of("UTC"));
    doReturn(fixedClock.instant()).when(clock).instant();
    doReturn(fixedClock.getZone()).when(clock).getZone();
  }

  @Test
  public void whenMakeReservationShouldSaveAndReturnReservationFromDB() {
    when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
    when(placeStatusRepository.findByStatus(placeStatus.getStatus()))
        .thenReturn(placeStatus);
    when(reservationRepository.save(reservation)).thenReturn(reservation);
    Reservation savedReservation = reservationService
        .makeReservation(reservation);

    assertNotNull("Return null not saved reservation ", savedReservation);
    verify(reservationRepository).save(reservation);
  }

  @Test
  public void whenGetReservationsByParkingLotAndTimeReturnListOfReservationsThatMatchTime() {
    List<Reservation> reservations = new ArrayList<>();
    reservations.add(reservation);

    when(reservationRepository
        .findReservationsByParkingLotIdAndTime(parkingLot.getId(),
            Timestamp.valueOf(LocalDateTime.now(clock))))
        .thenReturn(reservations);
    List<Reservation> reservationsFromDB = reservationService
        .getReservationsByParkingLotAndTime(parkingLot.getId());
    assertThat("Expected reservations are not equal with reservations from DB ",
        reservations, is(reservationsFromDB));
  }

  @Test
  public void whenGetUserReservationsThenReturnListOfReservations() {
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

    List<Reservation> reservations = new ArrayList<>();
    reservations.add(this.reservation);
    reservations.add(reservation);

    when(reservationRepository
        .findReservationsByUsernameAndTime(user.getUsername(),
            Timestamp.valueOf(LocalDateTime.now(clock))))
        .thenReturn(reservations);
    List<Reservation> reservationsFromDB = reservationService
        .getUserReservations(user.getUsername());
    assertThat("Expected reservations are not equal with reservations from DB ",
        reservations, is(reservationsFromDB));
  }

  @Test
  public void whenDeleteUserReservationThenDeleteItAndSendDesiredReservation() {
    DesiredReservation desiredReservation = new DesiredReservation();
    desiredReservation.setStartDate(
        Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 16, 30, 0)));
    desiredReservation.setEndDate(
        Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 17, 30, 0)));
    desiredReservation.setUser(user);
    desiredReservation.setParkingLot(parkingLot);

    List<DesiredReservation> desiredReservations = new ArrayList<>();
    desiredReservations.add(desiredReservation);
    when(reservationRepository.findById(1L))
        .thenReturn(Optional.ofNullable(this.reservation));
    doNothing().when(reservationRepository).deleteById(1L);
    doNothing().when(mailSender)
        .sendDesiredAvailableReservation(desiredReservation);
    when(desiredReservationRepository.findDesiredReservationsWhichMatchDates(
        reservation.getStartDate(), reservation.getEndDate()))
        .thenReturn(Optional.of(desiredReservations));

    reservationService.deleteUserReservation(1L);

    verify(reservationRepository, times(1)).deleteById(1L);
    verify(mailSender, times(1))
        .sendDesiredAvailableReservation(desiredReservation);
  }

  @Test
  public void whenGetTotalPriceAndReservationsBetweenDatesThenReturnStatisticDto() {
    List<Reservation> reservations = new ArrayList<>();
    reservations.add(this.reservation);

    when(reservationRepository.findReservationsByParkingLotIdStartAndEndDates(
        reservation.getParkingLot().getId(),
        Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 16, 30, 0)),
        Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 17, 30, 0))))
        .thenReturn(Optional.of(reservations));

    StatisticDTO statisticDTO = reservationService
        .getTotalPriceAndReservationsBetweenDates(
            reservation.getParkingLot().getId(),
            LocalDateTime.of(2019, 11, 11, 16, 30, 0),
            LocalDateTime.of(2019, 11, 11, 17, 30, 0));

    assertNotNull("StatisticDTO is null ", statisticDTO);
    assertEquals("Total price is not as expected ", reservation.getPrice(),
        statisticDTO.getTotalAmount());
    assertEquals("Total reservations is not as expected ",
        Long.valueOf(1L), statisticDTO.getTotalReservations());
  }
}
