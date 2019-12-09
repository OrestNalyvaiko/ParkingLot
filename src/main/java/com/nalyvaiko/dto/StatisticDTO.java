package com.nalyvaiko.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class StatisticDTO {

  private BigDecimal totalAmount;
  private Long totalReservations;

  public StatisticDTO() {
  }

  public StatisticDTO(BigDecimal totalAmount, Long totalReservations) {
    this.totalAmount = totalAmount;
    this.totalReservations = totalReservations;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public Long getTotalReservations() {
    return totalReservations;
  }

  public void setTotalReservations(Long totalReservations) {
    this.totalReservations = totalReservations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof StatisticDTO)) {
      return false;
    }
    StatisticDTO that = (StatisticDTO) o;
    return Objects.equals(getTotalAmount(), that.getTotalAmount()) &&
        Objects
            .equals(getTotalReservations(), that.getTotalReservations());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getTotalAmount(), getTotalReservations());
  }

  @Override
  public String toString() {
    return "StatisticDTO{" +
        "totalAmount=" + totalAmount +
        ", totalReservations=" + totalReservations +
        '}';
  }
}
