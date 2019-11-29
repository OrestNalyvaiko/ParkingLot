package com.nalyvaiko.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.nalyvaiko.model.Role;
import com.nalyvaiko.model.User;
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
public class UserRepositoryTest {

  private User user;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private TestEntityManager entityManager;

  @Before
  public void setup() {
    Role role = new Role();
    role.setRoleName("ROLE_USER");
    entityManager.persist(role);
    user = new User();
    user.setName("Orest");
    user.setSurname("Nalyvaiko");
    user.setUsername("orest");
    user.setPassword("123");
    user.setEmail("orest@gmail.com");
    user.setRole(role);
    entityManager.persistAndFlush(user);
  }

  @Test
  public void whenFindByUsernameThenReturnUser() {
    User userFromDB = userRepository.findByUsername(user.getUsername());

    assertNotNull("User from DB return null ", userFromDB);
    assertEquals("Saved and from DB users are not equal", user,
        userFromDB);
  }

  @Test
  public void whenExistsByEmailThenReturnTrue() {
    boolean isEmailExist = userRepository.existsByEmail(user.getEmail());

    assertTrue("Email is not exist ", isEmailExist);
  }

  @Test
  public void whenExistsByUsernameThenReturnTrue() {
    boolean isUsernameExist = userRepository
        .existsByUsername(user.getUsername());

    assertTrue("Username is not exist ", isUsernameExist);
  }
}
