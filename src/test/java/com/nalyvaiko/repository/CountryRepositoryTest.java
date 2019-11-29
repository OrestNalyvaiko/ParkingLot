package com.nalyvaiko.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.nalyvaiko.model.Country;
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
public class CountryRepositoryTest {

  @Autowired
  private CountryRepository countryRepository;
  @Autowired
  private TestEntityManager entityManager;

  @Test
  public void whenFindCountryByNameThenReturnCountry() {
    Country country = new Country();
    country.setCountryName("Ukraine");
    entityManager.persistAndFlush(country);
    Country countryFromDB = countryRepository
        .findCountryByCountryName(country.getCountryName());

    assertNotNull("Country entity from DB is null ", countryFromDB);
    assertEquals("Ids are not equal", country.getId(), countryFromDB.getId());
    assertEquals("Saved country name and country name from DB are not equal ",
        country.getCountryName(), countryFromDB.getCountryName());
  }
}
