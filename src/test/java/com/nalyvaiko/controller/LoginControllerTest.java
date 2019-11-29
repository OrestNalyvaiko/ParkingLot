package com.nalyvaiko.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nalyvaiko.security.JwtAuthenticationEntryPoint;
import com.nalyvaiko.security.JwtRequest;
import com.nalyvaiko.security.JwtResponse;
import com.nalyvaiko.util.JwtTokenUtil;
import com.nalyvaiko.security.UserDetailsServiceImpl;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
@Import(value = JwtAuthenticationEntryPoint.class)
public class LoginControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private AuthenticationManager authenticationManager;
  @MockBean
  private JwtTokenUtil jwtTokenUtil;
  @MockBean
  private UserDetailsServiceImpl userDetailsService;

  @Before
  public void setup() {
  }

  @Test
  public void whenAuthenticateUserValidInputThenReturnOK() throws Exception {
    JwtRequest jwtRequest = new JwtRequest("uliana1", "123");
    User user = new User("uliana1", "123",
        Collections.singletonList(() -> "ROLE_USER"));

    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        jwtRequest.getUsername(),
        jwtRequest.getPassword());
    UsernamePasswordAuthenticationToken authenticatedUsernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        user, "");

    when(
        authenticationManager.authenticate(usernamePasswordAuthenticationToken))
        .thenReturn(authenticatedUsernamePasswordAuthenticationToken);

    when(jwtTokenUtil.generateToken(
        (UserDetails) authenticatedUsernamePasswordAuthenticationToken
            .getPrincipal())).thenReturn("123.123.123");
    MvcResult mvcResult = mockMvc
        .perform(post("/authenticate")
            .content(objectMapper.writeValueAsString(jwtRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    JwtResponse jwtResponse = new JwtResponse("123.123.123");

    assertEquals("Expected response and response from controller is not equal",
        objectMapper.writeValueAsString(jwtResponse),
        mvcResult.getResponse().getContentAsString());
  }

  @Test
  public void whenAuthenticateUserInvalidInputThenReturnUnauthorized()
      throws Exception {
    JwtRequest jwtRequest = new JwtRequest("uliana2", "123");

    when(authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),
            jwtRequest.getPassword())))
        .thenThrow(new AccessDeniedException("Invalid username"));

    MvcResult mvcResult = mockMvc
        .perform(post("/authenticate")
            .content(objectMapper.writeValueAsString(jwtRequest))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andReturn();

    assertEquals("Actual and expected status is not equal", 401,
        mvcResult.getResponse().getStatus());

    assertEquals("Actual and expected error message is not equal",
        "Unauthorized",
        mvcResult.getResponse().getErrorMessage());
  }
}
