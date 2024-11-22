package com.fmi.rent.car.repositories;

import com.fmi.rent.car.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

    List<Car> findByCityIgnoreCaseAndIsDeletedFalse(String city);

    Car findByIdAndIsDeletedFalse(int id);

    Car findById(int id);
}