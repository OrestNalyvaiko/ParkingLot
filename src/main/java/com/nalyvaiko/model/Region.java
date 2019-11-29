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
@Table(name = "region")
public class Region {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "region_name", nullable = false)
  private String regionName;

  @ManyToOne
  @Cascade(value = {
      org.hibernate.annotations.CascadeType.PERSIST,
      org.hibernate.annotations.CascadeType.REFRESH,
      org.hibernate.annotations.CascadeType.MERGE,
      CascadeType.SAVE_UPDATE})
  @JoinColumn(name = "country_id")
  private Country country;

  public Region(Long id, String regionName, Country country) {
    this.id = id;
    this.regionName = regionName;
    this.country = country;
  }

  public Region() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getRegionName() {
    return regionName;
  }

  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  public Country getCountry() {
    return country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Region)) {
      return false;
    }
    Region region = (Region) o;
    return Objects.equals(getId(), region.getId()) &&
        Objects.equals(getRegionName(), region.getRegionName()) &&
        Objects.equals(getCountry(), region.getCountry());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getRegionName(), getCountry());
  }

  @Override
  public String toString() {
    return "Region{" +
        "id=" + id +
        ", regionName='" + regionName + '\'' +
        ", country=" + country +
        '}';
  }
}
