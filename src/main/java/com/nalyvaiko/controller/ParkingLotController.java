package com.nalyvaiko.controller;

import com.nalyvaiko.exception.SuchAddressExistException;
import com.nalyvaiko.model.ParkingLot;
import com.nalyvaiko.service.ParkingLotService;
import java.math.BigDecimal;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ParkingLotController {

  @Autowired
  private ParkingLotService parkingLotService;
  private static Logger logger = LogManager.getLogger();

  @GetMapping("/parkingLots")
  public ResponseEntity<?> getParkingLots() {
    logger.trace("Request for get parking lots is come");
    List<ParkingLot> parkingLots = parkingLotService.getParkingLots();
    logger.trace("Parking lots get from service");
    return ResponseEntity.ok(parkingLots);
  }

  @PostMapping("/parkingLot")
  public ResponseEntity<?> addParkingLot(@RequestBody ParkingLot parkingLot) {
    logger.trace("Add parking lot request");
    try {
      ParkingLot savedParkingLot = parkingLotService.addParkingLot(parkingLot);
      logger.trace("Parking lot is added");
      return ResponseEntity.ok(savedParkingLot);
    } catch (SuchAddressExistException e) {
      logger.warn("SuchAddressExistException with message " + e.getMessage());
      return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
  }

  @PutMapping("/parkingLots/{id}")
  public ResponseEntity<?> updateParkingLotTotalPlacesAndCostPerHour(
      @PathVariable Long id, @RequestParam("cost") BigDecimal costPerHour,
      @RequestParam("places") Integer places) {
    logger.trace("Update parking lot with id = " + id +
        " total places and cost per hour request");
    parkingLotService
        .updateParkingLotPlacesAndCostPerHour(id, places, costPerHour);
    logger.trace("Parking lot total places and cost per hour are updated");
    return new ResponseEntity(HttpStatus.OK);
  }
}
