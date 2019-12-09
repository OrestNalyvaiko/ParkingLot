package com.nalyvaiko.service;

import com.nalyvaiko.dto.StatisticDTO;
import com.nalyvaiko.mail.MailSender;
import com.nalyvaiko.model.PlaceStatus;
import com.nalyvaiko.model.Reservation;
import com.nalyvaiko.model.User;
import com.nalyvaiko.repository.DesiredReservationRepository;
import com.nalyvaiko.repository.PlaceStatusRepository;
import com.nalyvaiko.repository.ReservationRepository;
import com.nalyvaiko.repository.UserRepository;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReservationService {

  @Autowired
  private ReservationRepository reservationRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PlaceStatusRepository placeStatusRepository;
  @Autowired
  private DesiredReservationRepository desiredReservationRepository;
  @Autowired
  private MailSender mailSender;
  @Autowired
  private Clock clock;
  private static Logger logger = LogManager.getLogger();

  public Reservation makeReservation(Reservation reservation) {
    User user = userRepository
        .findByUsername(reservation.getUser().getUsername());
    reservation.setUser(user);
    PlaceStatus placeStatus = placeStatusRepository
        .findByStatus(reservation.getPlaceStatus().getStatus());
    reservation.setPlaceStatus(placeStatus);
    Reservation savedReservation = reservationRepository.save(reservation);
    logger.info("Reservation is saved to DB");
    return savedReservation;
  }

  public List<Reservation> getReservationsByParkingLotAndTime(
      Long parkingLotId) {
    LocalDateTime currentDateTime = LocalDateTime.now(clock);
    List<Reservation> reservationsByParkingLotIdAndTime = reservationRepository
        .findReservationsByParkingLotIdAndTime(parkingLotId,
            Timestamp.valueOf(currentDateTime));
    logger.info("Reservations by parking lot id = " + parkingLotId
        + " and current date time = " + currentDateTime + " are got from DB");
    return reservationsByParkingLotIdAndTime;
  }

  public List<Reservation> getUserReservations(String username) {
    LocalDateTime currentDateTime = LocalDateTime.now(clock);
    List<Reservation> userReservationsByUsernameAndTime = reservationRepository
        .findReservationsByUsernameAndTime(username,
            Timestamp.valueOf(currentDateTime));
    logger.info("Reservations by username = " + username
        + " and current date time = " + currentDateTime + " are got from DB");
    return userReservationsByUsernameAndTime;
  }

  public void deleteUserReservation(Long reservationId) {
    Reservation deletedReservation = reservationRepository
        .findById(reservationId).orElseThrow(RuntimeException::new);
    reservationRepository.deleteById(reservationId);
    logger.info("Reservation by id = " + reservationId + " is deleted from DB");
    desiredReservationRepository.findDesiredReservationsWhichMatchDates(
        deletedReservation.getStartDate(), deletedReservation.getEndDate())
        .ifPresent(r -> r.forEach(mailSender::sendDesiredAvailableReservation));
  }

  public StatisticDTO getTotalPriceAndReservationsBetweenDates(Long id,
      LocalDateTime startDate, LocalDateTime endDate) {
    List<Reservation> reservationsByParkingLotIdAndDates = reservationRepository
        .findReservationsByParkingLotIdStartAndEndDates(id,
            Timestamp.valueOf(startDate), Timestamp.valueOf(endDate))
        .orElse(new ArrayList<>());
    BigDecimal totalPrice = reservationsByParkingLotIdAndDates.stream()
        .map(Reservation::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    Long totalReservations = ((Integer) reservationsByParkingLotIdAndDates
        .size())
        .longValue();
    StatisticDTO statisticDTO = new StatisticDTO(totalPrice, totalReservations);
    logger.info("Statistic from " + startDate + " to " + endDate
        + " from parking lot with id " + id + " was got");
    return statisticDTO;
  }
}
