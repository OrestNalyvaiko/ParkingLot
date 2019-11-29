package com.nalyvaiko.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class AdminController {

  private static Logger logger = LogManager.getLogger();

  @PostMapping("/admin")
  public ResponseEntity<?> accessAdmin() {
    logger.trace("Access to admin controller");
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
