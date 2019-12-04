package com.nalyvaiko.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nalyvaiko.exception.SuchAddressExistException;
import com.nalyvaiko.model.City;
import com.nalyvaiko.model.Country;
import com.nalyvaiko.model.ParkingLot;
import com.nalyvaiko.model.Region;
import com.nalyvaiko.security.JwtRequestFilter;
import com.nalyvaiko.security.UserDetailsServiceImpl;
import com.nalyvaiko.service.ParkingLotService;
import com.nalyvaiko.util.JwtTokenUtil;
import java.math.BigDecimal;
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
public class ParkingLotControllerTest {

  private ParkingLot parkingLot;
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
  private ParkingLotService parkingLotService;
  @MockBean
  private UserDetailsServiceImpl userDetailsService;

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
    parkingLot.setCostPerHour(new BigDecimal(25.0));
    parkingLot.setAddress("3 Kokorudzy");
    parkingLot.setCity(city);

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
  public void whenGetParkingLotsThenReturnOK() throws Exception {
    List<ParkingLot> parkingLots = new ArrayList<>();
    parkingLots.add(parkingLot);
    when(parkingLotService.getParkingLots()).thenReturn(parkingLots);

    mockMvc.perform(get("/parkingLots")
        .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());

    verify(parkingLotService, times(1)).getParkingLots();
  }

  @Test
  public void whenAddParkingLotWithNewLocationThenReturnOk() throws Exception {
    when(parkingLotService.addParkingLot(parkingLot)).thenReturn(parkingLot);

    mockMvc.perform(post("/parkingLot")
        .header("Authorization", "Bearer " + token)
        .content(objectMapper.writeValueAsString(parkingLot))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    verify(parkingLotService, times(1)).addParkingLot(parkingLot);
  }

  @Test
  public void whenAddParkingLotWithExistLocationThenReturnConflict()
      throws Exception {
    when(parkingLotService.addParkingLot(parkingLot)).thenThrow(
        new SuchAddressExistException());

    mockMvc.perform(post("/parkingLot")
        .header("Authorization", "Bearer " + token)
        .content(objectMapper.writeValueAsString(parkingLot))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict());

    verify(parkingLotService, times(1)).addParkingLot(parkingLot);
  }

  @Test
  public void whenUpdateParkingLotTotalPlacesAndCostPerHourThenReturnOK()
      throws Exception {
    doNothing().when(parkingLotService)
        .updateParkingLotPlacesAndCostPerHour(1L, 20,
            BigDecimal.valueOf(20.5));

    mockMvc.perform(put("/parkingLots/{id}", 1L)
        .param("cost", "20.5")
        .param("places", "20")
        .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk());

    verify(parkingLotService, times(1))
        .updateParkingLotPlacesAndCostPerHour(1L, 20,
            BigDecimal.valueOf(20.5));
  }
}
