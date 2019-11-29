package com.nalyvaiko.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nalyvaiko.exception.LoginOrEmailExistException;
import com.nalyvaiko.model.Role;
import com.nalyvaiko.model.User;
import com.nalyvaiko.security.JwtAuthenticationEntryPoint;
import com.nalyvaiko.service.UserService;
import com.nalyvaiko.util.JwtTokenUtil;
import com.nalyvaiko.security.UserDetailsServiceImpl;
import com.nalyvaiko.service.UserServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@WebMvcTest(RegistrationController.class)
@Import(value = JwtAuthenticationEntryPoint.class)
public class RegistrationControllerTest {

  private User user;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private UserService userService;
  @MockBean
  private UserDetailsServiceImpl userDetailsService;
  @MockBean
  private JwtTokenUtil jwtTokenUtil;

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
  }

  @Test
  public void whenSuccessUserRegistrationReturnOK() throws Exception {
    doNothing().when(userService).createUser(user);

    MvcResult mvcResult = mockMvc
        .perform(post("/register")
            .content(objectMapper.writeValueAsString(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andReturn();

    verify(userService, times(1)).createUser(user);
  }

  @Test
  public void whenLoginOrEmailExistsReturnConflict() throws Exception {
    doThrow(new LoginOrEmailExistException()).when(userService)
        .createUser(user);

    MvcResult mvcResult = mockMvc
        .perform(post("/register")
            .content(objectMapper.writeValueAsString(user))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict()).andReturn();

    verify(userService, times(1)).createUser(user);
  }
}
