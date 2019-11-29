package com.nalyvaiko.repository;

import com.nalyvaiko.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

  Country findCountryByCountryName(String countryName);
}
