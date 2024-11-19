package com.fmi.rent.car.controllers;

import com.fmi.rent.car.entities.Car;
import com.fmi.rent.car.entities.Client;
import com.fmi.rent.car.services.ClientService;
import com.fmi.rent.car.services.CarService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private CarService carService;
    private ClientService clientService;
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(CarController.class); // Corrected Logger

    @Autowired
    public CarController(CarService carService, ClientService clientService, ObjectMapper objectMapper) {
        this.carService = carService;
        this.clientService = clientService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/list-by-client")
    public ResponseEntity<String> listCarsByClientCity(@RequestBody Client client) {

        String[] cities = {"plovdiv", "sofia", "varna", "burgas"};
        String city = null;
        for (String cityName : cities) {
            if (client.getAddress().toLowerCase().contains(cityName.toLowerCase())) {
                city = cityName;
                break;
            }
        }
        if (city == null) {
            String errorMessage = "Client's city is not available for car renting. Allowed cities: plovdiv, sofia, varna, burgas.";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        Client registeredClient = clientService.registerClient(client);
        List<Car> availableCarsByCity = carService.findCarsByClientAddress(city);

        try {
            String carsJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(availableCarsByCity);
            return new ResponseEntity<>(carsJson, HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error processing cars data.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable int id) {
        Car car = carService.findCarById(id);
        if (car == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if car not found
        }
        return new ResponseEntity<>(car, HttpStatus.OK); // Return 200 and the car object
    }

    @PostMapping
    public ResponseEntity<Car> addNewCar(@RequestBody Car car) {
        // Save the new car using the service
        Car savedCar = carService.saveCar(car);

        // Return the saved car along with a 201 CREATED status
        return new ResponseEntity<>(savedCar, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable int id, @RequestBody Car updatedCar) {
        Car existingCar = carService.findCarById(id);
        if (existingCar == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingCar.setModel(updatedCar.getModel());
        existingCar.setColor(updatedCar.getColor());
        existingCar.setCity(updatedCar.getCity());
        existingCar.setRentPerDay(updatedCar.getRentPerDay());

        carService.saveCar(existingCar);

        return new ResponseEntity<>(existingCar, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable int id) {
        Car carToDelete = carService.findCarById(id);
        if (carToDelete == null) {
            return new ResponseEntity<>("Car not found with ID " + id, HttpStatus.NOT_FOUND);
        }

        try {
            carService.deleteCar(id);
            return new ResponseEntity<>("Car with ID " + id + " has been deleted.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while deleting the car.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
