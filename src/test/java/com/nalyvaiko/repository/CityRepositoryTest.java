package com.nalyvaiko.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.nalyvaiko.model.City;
import com.nalyvaiko.model.Country;
import com.nalyvaiko.model.Region;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class CityRepositoryTest {

  private City city;
  @Autowired
  private CityRepository cityRepository;
  @Autowired
  private TestEntityManager entityManager;

  @Before
  public void setup() {
    Country country = new Country();
    country.setCountryName("Україна");
    Region region = new Region();
    region.setRegionName("Львівська");
    region.setCountry(country);
    city = new City();
    city.setCityName("Львів");
    city.setRegion(region);
    entityManager.persistAndFlush(city);
  }

  @Test
  public void whenFindCityByCityNameAndRegionIdThenReturnCity() {
    City cityFromDB = cityRepository
        .findCityByCityNameAndRegionId(city.getCityName(),
            city.getRegion().getId());

    assertNotNull("City entity from db is null ", cityFromDB);
    assertEquals("Ids are not equal", city.getId(), cityFromDB.getId());
  }
}