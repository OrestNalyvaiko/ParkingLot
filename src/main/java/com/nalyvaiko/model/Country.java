package com.nalyvaiko.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "country")
public class Country {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "country_name", nullable = false)
  private String countryName;

  public Country() {
  }

  public Country(Long id, String countryName) {
    this.id = id;
    this.countryName = countryName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCountryName() {
    return countryName;
  }

  public void setCountryName(String countryName) {
    this.countryName = countryName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Country)) {
      return false;
    }
    Country country = (Country) o;
    return Objects.equals(getId(), country.getId()) &&
        Objects.equals(getCountryName(), country.getCountryName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getCountryName());
  }

  @Override
  public String toString() {
    return "Country{" +
        "id=" + id +
        ", countryName='" + countryName + '\'' +
        '}';
  }
}



