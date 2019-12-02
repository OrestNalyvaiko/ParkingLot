package com.nalyvaiko.service;

import com.nalyvaiko.model.DesiredReservation;
import com.nalyvaiko.repository.DesiredReservationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DesiredReservationService {

  @Autowired
  private DesiredReservationRepository desiredReservationRepository;
  private static Logger logger = LogManager.getLogger();

  public DesiredReservation makeDesiredReservation(
      DesiredReservation desiredReservation) {
    DesiredReservation savedDesiredReservation = desiredReservationRepository
        .save(desiredReservation);
    logger.info("Desired reservation is saved to DB");
    return savedDesiredReservation;
  }

  public void deleteDesiredReservation(Long id) {
    desiredReservationRepository.deleteById(id);
    logger.info("Desired reservation is deleted from DB");
  }
}
