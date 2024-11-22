package com.fmi.rent.car.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fmi.rent.car.entities.Car;
import com.fmi.rent.car.entities.Client;
import com.fmi.rent.car.services.CarService;
import com.fmi.rent.car.services.ClientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private CarService carService;
    private ClientService clientService;
    private final ObjectMapper objectMapper;
    private final String[] VALID_CITIES = {"Plovdiv", "Sofia", "Varna", "Burgas"};
    private static final Logger logger = LoggerFactory.getLogger(CarController.class);

    @Autowired
    public CarController(CarService carService, ClientService clientService, ObjectMapper objectMapper) {
        this.carService = carService;
        this.clientService = clientService;
        this.objectMapper = objectMapper;
    }

    /* Системата предлага автомобили само и единствено базирани на локацията на клиента */

    // Листване на всички автомобили, на базата на локацията на клиента
    @PostMapping("/list-by-client")
    public ResponseEntity<Object> listCarsByClientCity(@Valid @RequestBody Client client) {
        logger.info("called listCarsByClientCity()");

        // Валидиране на града на клиента
        String city = null;
        for (String cityName : VALID_CITIES) {
            if (client.getAddress().toLowerCase().contains(cityName.toLowerCase())) {
                city = cityName;
                break;
            }
        }
        if (city == null) {
            String errorMessage = "Client's city is not available for car renting. Allowed cities: Plovdiv, Sofia, Varna, Burgas.";
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }

        // Запазване на данните за клиента
        Client registeredClient = clientService.registerClient(client);

        // Намиране на коли според града на клиента
        List<Car> availableCarsByCity = carService.findCarsByClientAddress(city);

        return new ResponseEntity<>(availableCarsByCity, HttpStatus.CREATED);
    }

    // Листване на конкретен автомобил по ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCarById(@PathVariable int id) {
        Car car = carService.findCarById(id);
        if (car == null) {
            return new ResponseEntity<>("Car not found.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    /*системата не трябва да допуска вариант при който се добя или
    актуализира автомобил извън предварително зададените градове.*/

    //Добавяне на нов автомобил
    @PostMapping
    public ResponseEntity<Object> addNewCar(@Valid @RequestBody Car car) {
        // Предпазва от презаписване на вече съществуващ автомобил
        if (car.getId() != 0) {
            return new ResponseEntity<>("ID should not be provided for a new car!", HttpStatus.BAD_REQUEST);
        }
        // Валидиране на града на колата
        boolean isCityValid = Arrays.stream(VALID_CITIES)
                .anyMatch(city -> car.getCity().toLowerCase().contains(city.toLowerCase()));

        if (!isCityValid) {
            return new ResponseEntity<>("City is not valid.", HttpStatus.BAD_REQUEST);
        }

        Car savedCar = carService.saveCar(car);
        return new ResponseEntity<>(savedCar, HttpStatus.CREATED);
    }

    // Актуализация на съществуващ автомобил
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCar(@PathVariable int id, @Valid @RequestBody Car updatedCar) {
        if (updatedCar.getId() != 0) {
            return new ResponseEntity<>("ID should be provided only in the URL!", HttpStatus.BAD_REQUEST);
        }

        Car existingCar = carService.findCarById(id);
        if (existingCar == null) {
            return new ResponseEntity<>("Car not found.", HttpStatus.NOT_FOUND);
        }

        boolean isCityValid = Arrays.stream(VALID_CITIES)
                .anyMatch(city -> updatedCar.getCity().toLowerCase().contains(city.toLowerCase()));

        if (!isCityValid) {
            return new ResponseEntity<>("City is not valid.", HttpStatus.BAD_REQUEST);
        }

        existingCar.setModel(updatedCar.getModel());
        existingCar.setColor(updatedCar.getColor());
        existingCar.setCity(updatedCar.getCity());
        existingCar.setRentPerDay(updatedCar.getRentPerDay());

        carService.saveCar(existingCar);

        return new ResponseEntity<>(existingCar, HttpStatus.OK);
    }

    // Изтриване на автомобил от системата
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCar(@PathVariable int id) {
        Car carToDelete = carService.findCarById(id);
        if (carToDelete == null || carToDelete.isDeleted()) {
            return new ResponseEntity<>("Car not found with ID " + id, HttpStatus.NOT_FOUND);
        }

        carService.softDeleteCar(id);
        return new ResponseEntity<>("Car with ID " + id + " has been softly deleted.", HttpStatus.OK);
    }


}
