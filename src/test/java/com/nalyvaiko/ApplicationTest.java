package com.nalyvaiko;

import com.nalyvaiko.controller.*;
import com.nalyvaiko.mail.MailSender;
import com.nalyvaiko.mail.MailSenderTest;
import com.nalyvaiko.repository.*;
import com.nalyvaiko.service.*;
import com.nalyvaiko.util.JwtTokenUtilTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({AdminControllerTest.class,
    DesiredReservationControllerTest.class,
    LoginControllerTest.class, ParkingLotControllerTest.class,
    RegistrationControllerTest.class,
    ReservationControllerTest.class, MailSenderTest.class,
    CityRepositoryTest.class,
    CountryRepositoryTest.class, DesiredReservationRepositoryTest.class,
    ParkingLotRepositoryTest.class, PlaceStatusRepositoryTest.class,
    RegionRepositoryTest.class, ReservationRepositoryTest.class,
    UserRepositoryTest.class, DesiredReservationServiceTest.class,
    ParkingLotServiceTest.class,
    ReservationServiceTest.class, UserServiceTest.class,
    JwtTokenUtilTest.class})
public class ApplicationTest {

}
