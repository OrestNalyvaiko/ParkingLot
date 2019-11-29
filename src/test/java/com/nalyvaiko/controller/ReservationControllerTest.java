package com.nalyvaiko.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nalyvaiko.model.*;
import com.nalyvaiko.security.JwtRequestFilter;
import com.nalyvaiko.security.UserDetailsServiceImpl;
import com.nalyvaiko.service.ReservationService;
import com.nalyvaiko.util.JwtTokenUtil;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ReservationControllerTest {

  private Reservation reservation;
  private String token;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private JwtTokenUtil jwtTokenUtil;
  @Autowired
  private JwtRequestFilter jwtRequestFilter;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;
  @MockBean
  private ReservationService reservationService;
  @MockBean
  private UserDetailsServiceImpl userDetailsService;

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
    PlaceStatus placeStatus = new PlaceStatus();
    placeStatus.setStatus("reserved");
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

    org.springframework.security.core.userdetails.User authenticatedUser = new org.springframework.security.core.userdetails.User(
        "user", bCryptPasswordEncoder.encode("password"),
        Collections.singleton(() -> "ROLE_USER"));
    when(userDetailsService.loadUserByUsername("user")).thenReturn(
        authenticatedUser);
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            "user",
            "password"));
    token = jwtTokenUtil
        .generateToken((UserDetails) authentication.getPrincipal());
  }

  @Test
  public void whenMakeReservationThenReturnOK() throws Exception {
    when(reservationService.makeReservation(reservation))
        .thenReturn(reservation);

    mockMvc.perform(post("/reservation")
        .header("Authorization", "Bearer " + token)
        .content(objectMapper.writeValueAsString(reservation))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    verify(reservationService, times(1)).makeReservation(reservation);
  }

  @Test
  public void whenGetReservationsByParkingLotIdThenReturnOK() throws Exception {
    List<Reservation> reservations = new ArrayList<>();
    reservation.setId(1L);
    reservations.add(reservation);
    when(reservationService.getReservationsByParkingLotAndTime(1L))
        .thenReturn(reservations);

    mockMvc.perform(get("/reservations/parkingLot/{id}", 1L)
        .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());

    verify(reservationService, times(1)).getReservationsByParkingLotAndTime(1L);
  }

  @Test
  public void whenGetUserReservationsThenReturnOK() throws Exception {
    List<Reservation> reservations = new ArrayList<>();
    reservations.add(reservation);
    when(reservationService
        .getUserReservations(reservation.getUser().getUsername()))
        .thenReturn(reservations);

    mockMvc.perform(
        get("/reservations/{username}", reservation.getUser().getUsername())
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());

    verify(reservationService, times(1))
        .getUserReservations(reservation.getUser().getUsername());
  }

  @Test
  public void whenCancelUserReservationsThenReturnOK() throws Exception {
    reservation.setId(1L);
    doNothing().when(reservationService)
        .deleteUserReservation(reservation.getId());

    mockMvc.perform(delete("/reservations/{id}", reservation.getId())
        .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());

    verify(reservationService, times(1))
        .deleteUserReservation(reservation.getId());
  }
}
