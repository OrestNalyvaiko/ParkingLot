package com.nalyvaiko.controller;

import com.nalyvaiko.model.Reservation;
import com.nalyvaiko.service.ReservationService;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ReservationController {

  @Autowired
  private ReservationService reservationService;
  private static Logger logger = LogManager.getLogger();

  @PostMapping("/reservation")
  public ResponseEntity<?> makeReservation(
      @RequestBody Reservation reservation) {
    logger.trace("Make reservation request");
    Reservation createdReservation = reservationService
        .makeReservation(reservation);
    logger.trace("Reservation is created");
    return ResponseEntity.ok(createdReservation);
  }

  @GetMapping("/reservations/parkingLot/{id}")
  public ResponseEntity<?> getReservationsByParkingLotId(
      @PathVariable("id") Long parkingLotId) {
    logger.trace("Get reservations by parking lot id = " + parkingLotId);
    List<Reservation> reservationsByParkingLotAndTime = reservationService
        .getReservationsByParkingLotAndTime(parkingLotId);
    logger.trace("Reservations are got from service");
    return ResponseEntity.ok(reservationsByParkingLotAndTime);
  }

  @GetMapping("/reservations/{username}")
  public ResponseEntity<?> getUserReservations(
      @PathVariable("username") String username) {
    logger.trace("Get user reservations by username = " + username);
    List<Reservation> reservationsByUsernameAndTime = reservationService
        .getUserReservations(username);
    logger.trace("User reservations are got from service");
    return ResponseEntity.ok(reservationsByUsernameAndTime);
  }

  @DeleteMapping("/reservations/{id}")
  public ResponseEntity<?> cancelUserReservation(
      @PathVariable("id") Long reservationId) {
    logger.trace("Cancel user reservation by id = " + reservationId);
    reservationService.deleteUserReservation(reservationId);
    logger.trace("Reservation is deleted");
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
