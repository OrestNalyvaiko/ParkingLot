package com.nalyvaiko.service;

import static org.mockito.Mockito.when;

import com.nalyvaiko.exception.LoginOrEmailExistException;
import com.nalyvaiko.model.Role;
import com.nalyvaiko.model.User;
import com.nalyvaiko.repository.RoleRepository;
import com.nalyvaiko.repository.UserRepository;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

  private User user;
  @InjectMocks
  private UserService userService;
  @Mock
  private PasswordEncoder bcryptEncoder;
  @Mock
  private UserRepository userRepository;
  @Mock
  private RoleRepository roleRepository;

  @Before
  public void setup() {
    user = new User();
    user.setName("Orest");
    user.setSurname("Nalyvaiko");
    user.setUsername("orest");
    user.setPassword("123");
    user.setEmail("orest@gmail.com");
  }

  @Test
  public void whenCreateUserShouldSaveToDB() throws Exception {
    when(bcryptEncoder.encode(user.getPassword()))
        .thenReturn("encodedPassword");
    when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
    when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
    Role role = new Role();
    role.setId(1L);
    role.setRoleName("ROLE_USER");
    when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
    when(userRepository.save(user)).thenReturn(user);

    userService.createUser(user);
  }

  @Test(expected = LoginOrEmailExistException.class)
  public void whenCreateUserWithExistUsernameThenReturnThrowLoginOrEmailExistException()
      throws Exception {
    when(bcryptEncoder.encode(user.getPassword()))
        .thenReturn("encodedPassword");
    when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);
    userService.createUser(user);
  }

  @Test(expected = LoginOrEmailExistException.class)
  public void whenCreateUserWithExistEmailThenReturnThrowLoginOrEmailExistException()
      throws Exception {
    when(bcryptEncoder.encode(user.getPassword()))
        .thenReturn("encodedPassword");
    when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
    when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
    userService.createUser(user);
  }

}
