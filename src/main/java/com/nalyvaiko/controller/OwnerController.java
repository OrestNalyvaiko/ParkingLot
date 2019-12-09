package com.nalyvaiko.controller;

import com.nalyvaiko.dto.StatisticDTO;
import com.nalyvaiko.service.ReservationService;
import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class OwnerController {

  private static Logger logger = LogManager.getLogger();
  @Autowired
  private ReservationService reservationService;

  @GetMapping("/parkingLot/{id}/statisticBetweenDates")
  public ResponseEntity<?> getStatisticBetweenDates(@PathVariable Long id,
      @RequestParam("startDate")
      @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime startDate,
      @RequestParam("endDate")
      @DateTimeFormat(iso = ISO.DATE_TIME) LocalDateTime endDate) {
    logger.trace(
        "Statistic request from " + startDate + " to " + endDate + " is come");
    StatisticDTO statisticDTO = reservationService
        .getTotalPriceAndReservationsBetweenDates(id,
            startDate, endDate);
    logger.trace("Statistic was got");
    return ResponseEntity.ok(statisticDTO);
  }
}
