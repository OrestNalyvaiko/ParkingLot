package com.nalyvaiko.security;

import com.nalyvaiko.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private PasswordEncoder bCryptPasswordEncoder;
  @Autowired
  private UserRepository userRepository;
  private static Logger logger = LogManager.getLogger();

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    logger.info("Load user by username = " + username);
    com.nalyvaiko.model.User userEntity = userRepository
        .findByUsername(username);
    if (userEntity == null) {
      logger.warn("User not found with username: " + username);
      throw new UsernameNotFoundException(
          "User not found with username: " + username);
    }
    return new User(userEntity.getUsername(), userEntity.getPassword(),
        getAuthority(userEntity));
  }

  private Collection<? extends GrantedAuthority> getAuthority(
      com.nalyvaiko.model.User user) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName()));
    return authorities;
  }
}
