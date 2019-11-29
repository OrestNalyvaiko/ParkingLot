package com.nalyvaiko.service;

import com.nalyvaiko.exception.LoginOrEmailExistException;
import com.nalyvaiko.model.User;
import com.nalyvaiko.repository.RoleRepository;
import com.nalyvaiko.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

  private static final Long USER_ROLE_ID = 1L;
  @Autowired
  private PasswordEncoder bcryptEncoder;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;
  private static Logger logger = LogManager.getLogger();

  public void createUser(User user) throws LoginOrEmailExistException {
    user.setPassword(bcryptEncoder.encode(user.getPassword()));
    if (userRepository.existsByUsername(user.getUsername())
        || userRepository.existsByEmail(user.getEmail())) {
      logger.warn("LoginOrEmailExistException is thrown");
      throw new LoginOrEmailExistException();
    }
    roleRepository.findById(USER_ROLE_ID).ifPresent(user::setRole);
    userRepository.save(user);
    logger.info("User is saved to DB");
  }
}
