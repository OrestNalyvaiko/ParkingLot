package com.nalyvaiko.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import com.nalyvaiko.exception.SuchAddressExistException;
import com.nalyvaiko.model.City;
import com.nalyvaiko.model.Country;
import com.nalyvaiko.model.ParkingLot;
import com.nalyvaiko.model.Region;
import com.nalyvaiko.repository.CityRepository;
import com.nalyvaiko.repository.CountryRepository;
import com.nalyvaiko.repository.ParkingLotRepository;
import com.nalyvaiko.repository.RegionRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ParkingLotServiceTest {

  private ParkingLot parkingLot;
  private Country country;
  private City city;
  private Region region;
  @Mock
  private ParkingLotRepository parkingLotRepository;
  @Mock
  private CountryRepository countryRepository;
  @Mock
  private RegionRepository regionRepository;
  @Mock
  private CityRepository cityRepository;
  @InjectMocks
  private ParkingLotService parkingLotService;

  @Before
  public void setup() {
    country = new Country();
    country.setCountryName("Ukraine");
    region = new Region();
    region.setRegionName("Lvivska");
    region.setCountry(country);
    city = new City();
    city.setCityName("Lviv");
    city.setRegion(region);
    parkingLot = new ParkingLot();
    parkingLot.setLng(new BigDecimal(12.345));
    parkingLot.setLat(new BigDecimal(12.345));
    parkingLot.setTotalPlaces(20);
    parkingLot.setCostPerHour(new BigDecimal(25.0));
    parkingLot.setAddress("3 Kokorudzy");
    parkingLot.setCity(city);
  }

  @Test
  public void whenGetParkingLotsReturnListOfParkingLots() {
    List<ParkingLot> parkingLots = new ArrayList<>();
    parkingLots.add(parkingLot);
    when(parkingLotRepository.findAll()).thenReturn(parkingLots);
    List<ParkingLot> parkingLotsFromDB = parkingLotService.getParkingLots();

    assertThat("Expected parking lots are not equal with parking lots from DB ",
        parkingLots, is(parkingLotsFromDB));
  }

  @Test
  public void whenAddParkingLotWithSameCityRegionCountryThenSaveToDB()
      throws Exception {
    Country country = new Country();
    country.setCountryName("Ukraine");
    Region region = new Region();
    region.setRegionName("Lvivska");
    region.setCountry(country);
    City city = new City();
    city.setCityName("Lviv");
    city.setRegion(region);
    ParkingLot parkingLot = new ParkingLot();
    parkingLot.setLng(new BigDecimal(12.345));
    parkingLot.setLat(new BigDecimal(12.345));
    parkingLot.setTotalPlaces(30);
    parkingLot.setCostPerHour(new BigDecimal(50.0));
    parkingLot.setAddress("10 Konovalza");
    parkingLot.setCity(city);

    when(parkingLotRepository.findParkingLotByAddressCityRegionCountryNames(
        parkingLot.getAddress(), city.getCityName(),
        region.getRegionName(), country.getCountryName()))
        .thenReturn(null);
    when(countryRepository.findCountryByCountryName(
        parkingLot.getCity().getRegion().getCountry().getCountryName()))
        .thenReturn(this.country);
    when(regionRepository.findRegionByRegionNameAndCountryId(
        parkingLot.getCity().getRegion().getRegionName(),
        parkingLot.getCity().getRegion().getCountry().getId()))
        .thenReturn(this.region);
    when(cityRepository
        .findCityByCityNameAndRegionId(parkingLot.getCity().getCityName(),
            parkingLot.getCity().getRegion().getId()))
        .thenReturn(this.city);
    when(parkingLotRepository.save(parkingLot)).thenReturn(parkingLot);

    ParkingLot savedParkingLot = parkingLotService.addParkingLot(parkingLot);

    assertSame("Parking lot returned from service is not same that expected",
        parkingLot, savedParkingLot);
    assertSame("Parking lot country is not same as expected", this.country,
        parkingLot.getCity().getRegion().getCountry());
    assertSame("Parking lot region is not same as expected", this.region,
        parkingLot.getCity().getRegion());
    assertSame("Parking lot city is not same as expected", this.city,
        parkingLot.getCity());
  }

  @Test(expected = SuchAddressExistException.class)
  public void whenAddParkingLotWithSameAddressThenThrowSuchAddressExistException()
      throws Exception {
    when(parkingLotRepository.findParkingLotByAddressCityRegionCountryNames(
        parkingLot.getAddress(), city.getCityName(),
        region.getRegionName(), country.getCountryName()))
        .thenReturn(parkingLot);

    parkingLotService.addParkingLot(parkingLot);
  }
}