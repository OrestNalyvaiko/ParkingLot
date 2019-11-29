package com.nalyvaiko.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.nalyvaiko.model.PlaceStatus;
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
public class PlaceStatusRepositoryTest {

  @Autowired
  private PlaceStatusRepository placeStatusRepository;
  @Autowired
  private TestEntityManager entityManager;

  @Test
  public void whenFindByStatusThenReturnPlaceStatus() {
    PlaceStatus placeStatus = new PlaceStatus();
    placeStatus.setStatus("Reserved");
    entityManager.persistAndFlush(placeStatus);

    PlaceStatus placeStatusFromDB = placeStatusRepository
        .findByStatus(placeStatus.getStatus());

    assertNotNull("Place status from DB is null ", placeStatusFromDB);
    assertEquals("Saved place status is not equal with place status from DB ",
        placeStatus.getStatus(), placeStatusFromDB.getStatus());
  }
}
