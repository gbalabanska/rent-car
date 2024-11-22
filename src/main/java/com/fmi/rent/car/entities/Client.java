package com.fmi.rent.car.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CLIENT")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name is required.")
    private String name;

    @NotEmpty(message = "Address is required.")
    private String address;

    @NotEmpty(message = "Phone is required.")
    private String phone;

    @NotNull(message = "Age is required.")
    private Integer age;  // Use Integer instead of int

    @NotNull(message = "Accidents information is required.")
    private Boolean hasAccidents;

    @JsonIgnore
    private boolean isDeleted;     // Defaults to false if not provided
}
