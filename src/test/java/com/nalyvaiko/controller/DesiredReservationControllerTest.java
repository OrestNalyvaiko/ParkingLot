package com.nalyvaiko.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nalyvaiko.model.City;
import com.nalyvaiko.model.Country;
import com.nalyvaiko.model.DesiredReservation;
import com.nalyvaiko.model.ParkingLot;
import com.nalyvaiko.model.Region;
import com.nalyvaiko.model.Role;
import com.nalyvaiko.model.User;
import com.nalyvaiko.security.JwtRequestFilter;
import com.nalyvaiko.security.UserDetailsServiceImpl;
import com.nalyvaiko.service.DesiredReservationService;
import com.nalyvaiko.util.JwtTokenUtil;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
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
public class DesiredReservationControllerTest {

  private DesiredReservation desiredReservation;
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
  private DesiredReservationService desiredReservationService;
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
  public void whenMakeDesiredReservationThenReturnOK() throws Exception {
    when(desiredReservationService.makeDesiredReservation(desiredReservation))
        .thenReturn(desiredReservation);

    mockMvc.perform(post("/desiredReservation")
        .header("Authorization", "Bearer " + token)
        .content(objectMapper.writeValueAsString(desiredReservation))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    verify(desiredReservationService, times(1))
        .makeDesiredReservation(desiredReservation);
  }

  @Test
  public void whenCancelDesiredReservationThenReturnOK() throws Exception {
    doNothing().when(desiredReservationService).deleteDesiredReservation(1L);

    mockMvc.perform(delete("/desiredReservations/{id}", 1L)
        .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andReturn();

    verify(desiredReservationService, times(1))
        .deleteDesiredReservation(1L);
  }
}

