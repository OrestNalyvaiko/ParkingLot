package com.nalyvaiko.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nalyvaiko.security.JwtRequestFilter;
import com.nalyvaiko.security.UserDetailsServiceImpl;
import com.nalyvaiko.service.DesiredReservationService;
import com.nalyvaiko.util.JwtTokenUtil;
import java.util.Collections;
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
public class AdminControllerTest {

  private String token;
  @Autowired
  private MockMvc mockMvc;
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
    org.springframework.security.core.userdetails.User authenticatedUser = new org.springframework.security.core.userdetails.User(
        "admin", bCryptPasswordEncoder.encode("password"),
        Collections.singleton(() -> "ROLE_ADMIN"));
    when(userDetailsService.loadUserByUsername("admin")).thenReturn(
        authenticatedUser);
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            "admin",
            "password"));
    token = jwtTokenUtil
        .generateToken((UserDetails) authentication.getPrincipal());
  }

  @Test
  public void whenAccessAdminWithCorrectRoleThenReturnOK() throws Exception {
    mockMvc.perform(post("/admin")
        .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  public void whenAccessAdminWithIncorrectRoleThenReturnForbidden()
      throws Exception {
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

    mockMvc.perform(post("/admin")
        .header("Authorization", "Bearer " + token))
        .andExpect(status().isForbidden())
        .andReturn();
  }
}
