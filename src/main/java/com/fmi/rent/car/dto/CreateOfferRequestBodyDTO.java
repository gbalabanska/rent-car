package com.fmi.rent.car.dto;

import lombok.Data;

@Data
public class CreateOfferRequestBodyDTO {
    private int carId;
    private int clientId;
    private int weekDays;
    private int weekendDays;
}
