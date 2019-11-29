package com.nalyvaiko.mail;

import com.nalyvaiko.model.DesiredReservation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component("customMailSender")
public class MailSender {

  private static Logger logger = LogManager.getLogger();
  @Autowired
  private JavaMailSender emailSender;

  public void sendDesiredAvailableReservation(
      DesiredReservation desiredReservation) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(desiredReservation.getUser().getEmail());
    message.setSubject("Available desired reservation.Reserve right now!");
    message.setText(createMessageText(desiredReservation));
    emailSender.send(message);
    logger
        .info("Message is send to " + desiredReservation.getUser().getEmail());
  }

  private String createMessageText(DesiredReservation desiredReservation) {
    StringBuilder text = new StringBuilder();
    text.append("Your desired reservation is know available.Reserve know!\n");
    text.append("Reservation on ");
    text.append(desiredReservation.getParkingLot().getAddress());
    text.append(" , ");
    text.append(desiredReservation.getParkingLot().getCity().getCityName());
    text.append(" , ");
    text.append(desiredReservation.getParkingLot().getCity().getRegion()
        .getRegionName());
    text.append(" , ");
    text.append(
        desiredReservation.getParkingLot().getCity().getRegion().getCountry()
            .getCountryName());
    text.append(" is know available!\n");
    return text.toString();
  }
}
