package com.nalyvaiko.repository;

import com.nalyvaiko.model.Reservation;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends
    JpaRepository<Reservation, Long> {

  @Query(value = "FROM Reservation r WHERE r.parkingLot.id = :parkingLotId AND r.endDate >= :currentTime")
  List<Reservation> findReservationsByParkingLotIdAndTime(Long parkingLotId,
      Timestamp currentTime);

  @Query(value = "FROM Reservation r WHERE r.user.username = :username AND r.endDate >= :currentTime")
  List<Reservation> findReservationsByUsernameAndTime(String username,
      Timestamp currentTime);
}
