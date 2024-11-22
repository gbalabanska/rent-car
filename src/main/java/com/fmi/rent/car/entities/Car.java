package com.fmi.rent.car.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CAR")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Model is required.")
    private String model;

    @NotEmpty(message = "Color is required.")
    private String color;

    @NotEmpty(message = "City is required.")
    private String city;

    @NotNull(message = "Rent per day is required.")
    @Positive(message = "Rent per day must be a positive value.")
    private Double rentPerDay;    // цена на ден за наем

    @JsonIgnore
    private boolean isDeleted;
}
