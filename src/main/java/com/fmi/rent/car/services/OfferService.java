package com.fmi.rent.car.services;

import com.fmi.rent.car.dto.CreateOfferRequestBodyDTO;
import com.fmi.rent.car.dto.CreateOfferResponseBodyDTO;
import com.fmi.rent.car.entities.Car;
import com.fmi.rent.car.entities.Client;
import com.fmi.rent.car.entities.Offer;
import com.fmi.rent.car.repositories.CarRepository;
import com.fmi.rent.car.repositories.ClientRepository;
import com.fmi.rent.car.repositories.OfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OfferService {

    private final CarRepository carRepository;
    private final ClientRepository clientRepository;
    private final OfferRepository offerRepository;

    public OfferService(CarRepository carRepository, ClientRepository clientRepository, OfferRepository offerRepository) {
        this.carRepository = carRepository;
        this.clientRepository = clientRepository;
        this.offerRepository = offerRepository;
    }

    @Transactional
    public CreateOfferResponseBodyDTO createOffer(CreateOfferRequestBodyDTO request) {
        CreateOfferResponseBodyDTO responseDTO = new CreateOfferResponseBodyDTO();

        Car car = carRepository.findByIdAndIsDeletedFalse(request.getCarId());
        if (car == null) {
            responseDTO.setErrorMsg("Car with id " + request.getCarId() + " was not found.");
            return responseDTO;
        }

        Client client = clientRepository.findByIdAndIsDeletedFalse(request.getClientId());
        if (client == null) {
            responseDTO.setErrorMsg("Client with id " + request.getClientId() + " was not found.");
            return responseDTO;
        }

        if (!car.getCity().equals(client.getAddress())) {
            responseDTO.setErrorMsg("Client " + client.getName() + " and car " + car.getModel() + " are in different cities!");
            return responseDTO;
        }

        // Изчисляване на цена
        double rentPerDay = car.getRentPerDay();
        double weekdayCost = rentPerDay * request.getWeekDays();
        double weekendCost = rentPerDay * request.getWeekendDays() * 1.1;
        double totalCost = weekdayCost + weekendCost;

        if (Boolean.TRUE.equals(client.getHasAccidents())) {
            totalCost += 200;
        }

        // Запазване на офертата
        Offer offer = new Offer();
        offer.setCarId(request.getCarId());
        offer.setClientId(request.getClientId());
        offer.setWeekDays(request.getWeekDays());
        offer.setWeekendDays(request.getWeekendDays());
        offer.setTotalCost(totalCost);
        offer.setIsAccepted(0); // Default to not accepted

        Offer savedOffer = offerRepository.save(offer);

        // Map to DetailedOfferResponseDTO
        responseDTO.setOfferId(savedOffer.getId());
        responseDTO.setWeekDays(savedOffer.getWeekDays());
        responseDTO.setWeekendDays(savedOffer.getWeekendDays());
        responseDTO.setTotalCost(savedOffer.getTotalCost());
        responseDTO.setIsAccepted(savedOffer.getIsAccepted());
        responseDTO.setClient(client);
        responseDTO.setCar(car);

        return responseDTO;
    }

    public List<Offer> getOffersByClientId(int clientId) {
        return offerRepository.findByClientId(clientId);
    }

    public Offer getOfferById(int offerId) {
        return offerRepository.findByIdAndIsDeletedFalse(offerId);
    }

    public boolean softDeleteOffer(int offerId) {
        Offer offer = offerRepository.findByIdAndIsDeletedFalse(offerId);
        if(offer != null){
            offer.setDeleted(true);
            offerRepository.save(offer);
            return true;
        }
        return false; // Offer not found
    }

    public Offer acceptOffer(int offerId) {
        Offer offer = offerRepository.findByIdAndIsDeletedFalse(offerId);
        if(offer == null){
            return null;
        }
        offer.setIsAccepted(1);
        return offerRepository.save(offer);
    }

}
