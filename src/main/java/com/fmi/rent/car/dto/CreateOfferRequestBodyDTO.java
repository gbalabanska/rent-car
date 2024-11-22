package com.fmi.rent.car.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateOfferRequestBodyDTO {

    @NotNull(message = "Car ID is required.")
    @Positive(message = "Car ID must be a positive value.")
    private Integer carId;

    @NotNull(message = "Client ID is required.")
    @Positive(message = "Client ID must be a positive value.")
    private Integer clientId;

    @NotNull(message = "Weekdays must be specified.")
    @Min(value = 0, message = "Weekdays cannot be negative.")
    private Integer weekDays;

    @NotNull(message = "Weekend days must be specified.")
    @Min(value = 0, message = "Weekend days cannot be negative.")
    private Integer weekendDays;
}

