package com.nalyvaiko.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "place_status")
public class PlaceStatus {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(name = "status", nullable = false)
  private String status;

  public PlaceStatus(Long id, String status) {
    this.id = id;
    this.status = status;
  }

  public PlaceStatus() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PlaceStatus)) {
      return false;
    }
    PlaceStatus that = (PlaceStatus) o;
    return Objects.equals(getId(), that.getId()) &&
        Objects.equals(getStatus(), that.getStatus());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getStatus());
  }

  @Override
  public String toString() {
    return "PlaceStatus{" +
        "id=" + id +
        ", status='" + status + '\'' +
        '}';
  }
}
