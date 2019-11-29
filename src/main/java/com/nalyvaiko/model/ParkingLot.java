package com.nalyvaiko.model;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "parking_lot")
public class ParkingLot {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "total_places", nullable = false)
  private Integer totalPlaces;

  @Column(name = "cost_hour", nullable = false)
  private BigDecimal costPerHour;

  @ManyToOne
  @Cascade(value = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE,
      CascadeType.SAVE_UPDATE})
  @JoinColumn(name = "city_id")
  private City city;

  @Column(name = "lat", nullable = false)
  private BigDecimal lat;

  @Column(name = "lng", nullable = false)
  private BigDecimal lng;

  public ParkingLot(Long id, String address, int totalPlaces,
      BigDecimal costPerHour, City city, BigDecimal lat, BigDecimal lng) {
    this.id = id;
    this.address = address;
    this.totalPlaces = totalPlaces;
    this.costPerHour = costPerHour;
    this.city = city;
    this.lat = lat;
    this.lng = lng;
  }

  public ParkingLot() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public int getTotalPlaces() {
    return totalPlaces;
  }

  public void setTotalPlaces(int totalPlaces) {
    this.totalPlaces = totalPlaces;
  }

  public BigDecimal getCostPerHour() {
    return costPerHour;
  }

  public void setCostPerHour(BigDecimal costPerHour) {
    this.costPerHour = costPerHour;
  }

  public BigDecimal getLat() {
    return lat;
  }

  public void setLat(BigDecimal lat) {
    this.lat = lat;
  }

  public BigDecimal getLng() {
    return lng;
  }

  public void setLng(BigDecimal lng) {
    this.lng = lng;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ParkingLot)) {
      return false;
    }
    ParkingLot that = (ParkingLot) o;
    return getTotalPlaces() == that.getTotalPlaces() &&
        Objects.equals(getId(), that.getId()) &&
        Objects.equals(getAddress(), that.getAddress()) &&
        Objects.equals(getCostPerHour(), that.getCostPerHour()) &&
        Objects.equals(getCity(), that.getCity()) &&
        Objects.equals(getLat(), that.getLat()) &&
        Objects.equals(getLng(), that.getLng());
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(getId(), getAddress(), getTotalPlaces(), getCostPerHour(),
            getCity(), getLat(), getLng());
  }

  @Override
  public String toString() {
    return "ParkingLot{" +
        "id=" + id +
        ", address='" + address + '\'' +
        ", totalPlaces=" + totalPlaces +
        ", costPerHour=" + costPerHour +
        ", city=" + city +
        ", lat=" + lat +
        ", lng=" + lng +
        '}';
  }
}
