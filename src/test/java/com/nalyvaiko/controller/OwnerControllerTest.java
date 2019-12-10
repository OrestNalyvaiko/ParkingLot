package com.nalyvaiko.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nalyvaiko.dto.StatisticDTO;
import com.nalyvaiko.security.JwtRequestFilter;
import com.nalyvaiko.security.UserDetailsServiceImpl;
import com.nalyvaiko.service.ReservationService;
import com.nalyvaiko.util.JwtTokenUtil;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class OwnerControllerTest {

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
  public void whenGetStatisticBetweenDatesThenReturnOK() throws Exception {
    when(reservationService.getTotalPriceAndReservationsBetweenDates(1L,
        LocalDateTime.of(2019, 11, 11, 16, 30, 0),
        LocalDateTime.of(2019, 11, 11, 17, 30, 0)))
        .thenReturn(
            new StatisticDTO(BigDecimal.valueOf(100), 10L, new HashMap<>()));

    mockMvc.perform(get("/parkingLot/{id}/statisticBetweenDates", 1L)
        .header("Authorization", "Bearer " + token)
        .param("startDate", "2019-11-11T16:30:00.000Z")
        .param("endDate", "2019-11-11T17:30:00.000Z"))
        .andExpect(status().isOk())
        .andReturn();

    verify(reservationService, times(1))
        .getTotalPriceAndReservationsBetweenDates(1L,
            LocalDateTime.of(2019, 11, 11, 16, 30, 0),
            LocalDateTime.of(2019, 11, 11, 17, 30, 0));
  }
}
