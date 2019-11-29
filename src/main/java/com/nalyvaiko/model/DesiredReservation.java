package com.nalyvaiko.model;

import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "desired_reservation")
public class DesiredReservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "start_date", nullable = false)
  private Timestamp startDate;

  @Column(name = "end_date", nullable = false)
  private Timestamp endDate;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "parking_lot_id")
  private ParkingLot parkingLot;

  public DesiredReservation() {
  }

  public DesiredReservation(Long id, Timestamp startDate,
      Timestamp endDate, User user, ParkingLot parkingLot) {
    this.id = id;
    this.startDate = startDate;
    this.endDate = endDate;
    this.user = user;
    this.parkingLot = parkingLot;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Timestamp getStartDate() {
    return startDate;
  }

  public void setStartDate(Timestamp startDate) {
    this.startDate = startDate;
  }

  public Timestamp getEndDate() {
    return endDate;
  }

  public void setEndDate(Timestamp endDate) {
    this.endDate = endDate;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public ParkingLot getParkingLot() {
    return parkingLot;
  }

  public void setParkingLot(ParkingLot parkingLot) {
    this.parkingLot = parkingLot;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DesiredReservation)) {
      return false;
    }
    DesiredReservation that = (DesiredReservation) o;
    return Objects.equals(getId(), that.getId()) &&
        Objects.equals(getStartDate(), that.getStartDate()) &&
        Objects.equals(getEndDate(), that.getEndDate()) &&
        Objects.equals(getUser(), that.getUser()) &&
        Objects.equals(getParkingLot(), that.getParkingLot());
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(getId(), getStartDate(), getEndDate(), getUser(),
            getParkingLot());
  }
}
