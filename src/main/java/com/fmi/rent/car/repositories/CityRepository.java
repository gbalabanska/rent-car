package com.fmi.rent.car.repositories;

import com.fmi.rent.car.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    @Query("SELECT c.name FROM City c") //find city names
    List<String> findAllCityNames();
}