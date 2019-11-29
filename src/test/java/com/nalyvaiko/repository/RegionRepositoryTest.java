package com.nalyvaiko.repository;

import static org.junit.Assert.assertNotNull;

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
public class RegionRepositoryTest {

  private Region region;
  @Autowired
  private RegionRepository regionRepository;
  @Autowired
  private TestEntityManager entityManager;

  @Before
  public void setup() {
    Country country = new Country();
    country.setCountryName("Україна");
    region = new Region();
    region.setCountry(country);
    region.setRegionName("Львівська");
    entityManager.persistAndFlush(region);
  }

  @Test
  public void whenFindRegionByRegionNameAndCountryIdThenReturnRegion() {
    Region regionFromDB = regionRepository.findRegionByRegionNameAndCountryId(
        region.getRegionName(), region.getCountry().getId());

    assertNotNull("Region from DB is null ", regionFromDB);
  }
}
