package com.nalyvaiko.mail;

import static org.junit.Assert.assertEquals;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.nalyvaiko.model.City;
import com.nalyvaiko.model.Country;
import com.nalyvaiko.model.DesiredReservation;
import com.nalyvaiko.model.ParkingLot;
import com.nalyvaiko.model.Region;
import com.nalyvaiko.model.Role;
import com.nalyvaiko.model.User;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.mail.internet.MimeMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class MailSenderTest {

  @Configuration
  static class ContextConfiguration {

    @Bean
    MailSender mailSender() {
      return new MailSender();
    }

    @Bean
    public JavaMailSender emailSender() {
      return new JavaMailSenderImpl();
    }
  }

  @Autowired
  private MailSender mailSender;
  private GreenMail smtpServer;
  private DesiredReservation desiredReservation;

  @Before
  public void setup() {
    smtpServer = new GreenMail(new ServerSetup(25, null, "smtp"));
    smtpServer.start();

    User user = new User();
    user.setName("Orest");
    user.setSurname("Nalyvaiko");
    user.setUsername("orest");
    user.setPassword("123");
    user.setEmail("log4j2lab@gmail.com");
    Role role = new Role();
    role.setRoleName("ROLE_USER");
    user.setRole(role);
    Country country = new Country();
    country.setCountryName("Ukraine");
    Region region = new Region();
    region.setRegionName("Lvivska");
    region.setCountry(country);
    City city = new City();
    city.setCityName("Lviv");
    city.setRegion(region);
    ParkingLot parkingLot = new ParkingLot();
    parkingLot.setId(1L);
    parkingLot.setLng(new BigDecimal(12.345));
    parkingLot.setLat(new BigDecimal(12.345));
    parkingLot.setTotalPlaces(20);
    parkingLot.setCostPerHour(new BigDecimal(25.0));
    parkingLot.setAddress("3 Kokorudzy");
    parkingLot.setCity(city);
    desiredReservation = new DesiredReservation();
    desiredReservation.setStartDate(
        Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 16, 30, 0)));
    desiredReservation.setEndDate(
        Timestamp.valueOf(LocalDateTime.of(2019, 11, 11, 17, 30, 0)));
    desiredReservation.setUser(user);
    desiredReservation.setParkingLot(parkingLot);
  }

  @Test
  public void whenSendDesiredAvailableReservationSendMail() throws Exception {
    mailSender.sendDesiredAvailableReservation(desiredReservation);
    MimeMessage[] receivedMessages = smtpServer.getReceivedMessages();
    assertEquals(1, receivedMessages.length);

    MimeMessage current = receivedMessages[0];

    assertEquals(desiredReservation.getUser().getEmail(),
        current.getAllRecipients()[0].toString());
  }

  @After
  public void close() {
    smtpServer.stop();
  }
}
