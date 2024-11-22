package com.fmi.rent.car.dto;

import com.fmi.rent.car.entities.Car;
import com.fmi.rent.car.entities.Client;
import lombok.Data;

@Data
public class CreateOfferResponseBodyDTO {
    private int offerId;
    private int weekDays;
    private int weekendDays;
    private double totalCost;
    private int isAccepted;

    private Client client;
    private Car car;

    private String errorMsg;
}
