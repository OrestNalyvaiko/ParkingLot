package com.nalyvaiko.controller;

import com.nalyvaiko.exception.LoginOrEmailExistException;
import com.nalyvaiko.model.User;
import com.nalyvaiko.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class RegistrationController {

  @Autowired
  private UserService userService;
  private static Logger logger = LogManager.getLogger();

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody User user) {
    logger.trace("Register user request is come");
    try {
      userService.createUser(user);
      logger.trace("User is registered");
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (LoginOrEmailExistException e) {
      logger.warn("LoginOrEmailExistException with message " + e.getMessage());
      return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
  }


}
