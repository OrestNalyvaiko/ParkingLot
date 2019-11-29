package com.nalyvaiko.repository;

import com.nalyvaiko.model.PlaceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceStatusRepository extends
    JpaRepository<PlaceStatus, Long> {

  PlaceStatus findByStatus(String status);
}
