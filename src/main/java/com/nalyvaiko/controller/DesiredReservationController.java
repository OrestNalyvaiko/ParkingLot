package com.nalyvaiko.controller;

import com.nalyvaiko.model.DesiredReservation;
import com.nalyvaiko.service.DesiredReservationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class DesiredReservationController {

  @Autowired
  private DesiredReservationService desiredReservationService;
  private static Logger logger = LogManager.getLogger();

  @PostMapping("/desiredReservation")
  public ResponseEntity<?> makeDesiredReservation(@RequestBody
      DesiredReservation desiredReservation) {
    logger.trace("Make desired reservation request");
    DesiredReservation createdDesiredReservation = desiredReservationService
        .makeDesiredReservation(desiredReservation);
    logger.trace("Desired reservation is created");
    return ResponseEntity.ok(desiredReservation);
  }
}
