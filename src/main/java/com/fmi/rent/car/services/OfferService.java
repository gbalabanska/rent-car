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
import java.util.Optional;

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
        // Fetch the car and client details
        Car car = carRepository.findById(request.getCarId());
        if (car == null) {
            throw new IllegalArgumentException("Invalid car ID");
        }
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid client ID"));

        // Validate city match
        if (!car.getCity().equals(client.getAddress())) {
            throw new IllegalArgumentException("Client and car must be in the same city");
        }

        // Calculate total cost
        double rentPerDay = car.getRentPerDay();
        double weekdayCost = rentPerDay * request.getWeekDays();
        double weekendCost = rentPerDay * request.getWeekendDays() * 1.1;
        double totalCost = weekdayCost + weekendCost;

        // Add accident surcharge if applicable
        if (client.isHasAccidents()) {
            totalCost += 200;
        }

        // Create and save the offer
        Offer offer = new Offer();
        offer.setCarId(request.getCarId());
        offer.setClientId(request.getClientId());
        offer.setWeekDays(request.getWeekDays());
        offer.setWeekendDays(request.getWeekendDays());
        offer.setTotalCost(totalCost);
        offer.setIsAccepted(0); // Default to not accepted

        Offer savedOffer = offerRepository.save(offer);

        // Map to DetailedOfferResponseDTO
        CreateOfferResponseBodyDTO responseDTO = new CreateOfferResponseBodyDTO();
        responseDTO.setOfferId(savedOffer.getOfferId());
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
        return offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found with ID: " + offerId));
    }

    public boolean softDeleteOffer(int offerId) {
        Optional<Offer> offerOptional = offerRepository.findById(offerId);
        if (offerOptional.isPresent()) {
            Offer offer = offerOptional.get();
            offer.setDeleted(true); // Set the isDeleted flag to true
            offerRepository.save(offer); // Save the updated offer
            return true;
        }
        return false; // Offer not found
    }

    public Offer acceptOffer(int offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("Offer not found with ID: " + offerId));
        offer.setIsAccepted(1); // Set `isAccepted` to true (1)
        return offerRepository.save(offer);
    }

}
