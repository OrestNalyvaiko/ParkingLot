package com.nalyvaiko.repository;

import com.nalyvaiko.model.DesiredReservation;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DesiredReservationRepository extends
    JpaRepository<DesiredReservation, Long> {

  @Query(value = "SELECT d FROM DesiredReservation d "
      + "WHERE d.startDate >= :startDate "
      + "AND d.endDate <= :endDate")
  Optional<List<DesiredReservation>> findDesiredReservationsWhichMatchDates(
      Timestamp startDate, Timestamp endDate);
}
