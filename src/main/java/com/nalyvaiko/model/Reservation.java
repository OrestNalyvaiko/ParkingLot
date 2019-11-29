package com.nalyvaiko.model;

import java.math.BigDecimal;
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
@Table(name = "reservation")
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "place", nullable = false)
  private Integer place;

  @Column(name = "start_date", nullable = false)
  private Timestamp startDate;

  @Column(name = "end_date", nullable = false)
  private Timestamp endDate;

  @Column(name = "price", nullable = false)
  private BigDecimal price;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "parking_lot_id")
  private ParkingLot parkingLot;

  @ManyToOne
  @JoinColumn(name = "place_status_id")
  private PlaceStatus placeStatus;

  public Reservation(Long id, Integer place, Timestamp startDate,
      Timestamp endDate, BigDecimal price, User user,
      ParkingLot parkingLot, PlaceStatus placeStatus) {
    this.id = id;
    this.place = place;
    this.startDate = startDate;
    this.endDate = endDate;
    this.price = price;
    this.user = user;
    this.parkingLot = parkingLot;
    this.placeStatus = placeStatus;
  }

  public Reservation() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getPlace() {
    return place;
  }

  public void setPlace(Integer place) {
    this.place = place;
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

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
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

  public PlaceStatus getPlaceStatus() {
    return placeStatus;
  }

  public void setPlaceStatus(PlaceStatus placeStatus) {
    this.placeStatus = placeStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Reservation)) {
      return false;
    }
    Reservation that = (Reservation) o;
    return Objects.equals(getId(), that.getId()) &&
        Objects.equals(getPlace(), that.getPlace()) &&
        Objects.equals(getStartDate(), that.getStartDate()) &&
        Objects.equals(getEndDate(), that.getEndDate()) &&
        Objects.equals(getPrice(), that.getPrice()) &&
        Objects.equals(getUser(), that.getUser()) &&
        Objects.equals(getParkingLot(), that.getParkingLot()) &&
        Objects.equals(getPlaceStatus(), that.getPlaceStatus());
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(getId(), getPlace(), getStartDate(), getEndDate(), getPrice(),
            getUser(), getParkingLot(), getPlaceStatus());
  }

  @Override
  public String toString() {
    return "Reservation{" +
        "id=" + id +
        ", place=" + place +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        ", price=" + price +
        ", user=" + user +
        ", parkingLot=" + parkingLot +
        ", placeStatus=" + placeStatus +
        '}';
  }
}
