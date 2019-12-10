package com.nalyvaiko.dto;

import com.nalyvaiko.model.User;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class StatisticDTO {

  private BigDecimal totalAmount;
  private Long totalReservations;
  private Map<User, Double> usersPercentageOfTotalReservations;

  public StatisticDTO() {
  }

  public StatisticDTO(BigDecimal totalAmount, Long totalReservations,
      Map<User, Double> usersPercentageOfTotalReservations) {
    this.totalAmount = totalAmount;
    this.totalReservations = totalReservations;
    this.usersPercentageOfTotalReservations = usersPercentageOfTotalReservations;
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

  public Map<User, Double> getUsersPercentageOfTotalReservations() {
    return usersPercentageOfTotalReservations;
  }

  public void setUsersPercentageOfTotalReservations(
      Map<User, Double> usersPercentageOfTotalReservations) {
    this.usersPercentageOfTotalReservations = usersPercentageOfTotalReservations;
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
            .equals(getTotalReservations(), that.getTotalReservations()) &&
        Objects.equals(getUsersPercentageOfTotalReservations(),
            that.getUsersPercentageOfTotalReservations());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getTotalAmount(), getTotalReservations(),
        getUsersPercentageOfTotalReservations());
  }

  @Override
  public String toString() {
    return "StatisticDTO{" +
        "totalAmount=" + totalAmount +
        ", totalReservations=" + totalReservations +
        ", usersPercentageOfTotalReservations="
        + usersPercentageOfTotalReservations +
        '}';
  }
}
