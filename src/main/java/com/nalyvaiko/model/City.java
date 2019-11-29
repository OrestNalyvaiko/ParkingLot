package com.nalyvaiko.model;

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
@Table(name = "city")
public class City {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "city_name", nullable = false)
  private String cityName;

  @ManyToOne
  @Cascade(value = {
      org.hibernate.annotations.CascadeType.PERSIST,
      org.hibernate.annotations.CascadeType.REFRESH,
      org.hibernate.annotations.CascadeType.MERGE,
      CascadeType.SAVE_UPDATE})
  @JoinColumn(name = "region_id")
  private Region region;

  public City(Long id, String cityName, Region region) {
    this.id = id;
    this.cityName = cityName;
    this.region = region;
  }

  public City() {

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public Region getRegion() {
    return region;
  }

  public void setRegion(Region region) {
    this.region = region;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof City)) {
      return false;
    }
    City city = (City) o;
    return Objects.equals(getId(), city.getId()) &&
        Objects.equals(getCityName(), city.getCityName()) &&
        Objects.equals(getRegion(), city.getRegion());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getCityName(), getRegion());
  }

  @Override
  public String toString() {
    return "City{" +
        "id=" + id +
        ", cityName='" + cityName + '\'' +
        ", region=" + region +
        '}';
  }
}
