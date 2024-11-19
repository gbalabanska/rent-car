package com.fmi.rent.car.services;

import com.fmi.rent.car.entities.Car;
import com.fmi.rent.car.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> findCarsByClientAddress(String city) {
        return carRepository.findByCityIgnoreCase(city);
    }

    public Car findCarById(int id) {
        return carRepository.findById(id);
    }

    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    public void softDeleteCar(int id) {
        Car car = carRepository.findById(id);
        if (car != null) {
            car.setDeleted(true); // Set the `is_deleted` flag to true
            carRepository.save(car); // Persist the change
        } else {
            throw new IllegalArgumentException("Car not found with ID " + id);
        }
    }


}
