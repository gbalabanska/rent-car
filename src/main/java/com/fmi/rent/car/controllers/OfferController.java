package com.fmi.rent.car.controllers;

import com.fmi.rent.car.dto.CreateOfferRequestBodyDTO;
import com.fmi.rent.car.dto.CreateOfferResponseBodyDTO;
import com.fmi.rent.car.entities.Offer;
import com.fmi.rent.car.services.OfferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offers")
public class OfferController {

    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    // Създаване на нова оферта с данни за потребителя, модела на автомобила и дните за наемане
    @PostMapping("/create")
    public ResponseEntity<CreateOfferResponseBodyDTO> createOffer(@Valid @RequestBody CreateOfferRequestBodyDTO request) {
        CreateOfferResponseBodyDTO detailedOfferResponseDTO = offerService.createOffer(request);
        return ResponseEntity.ok(detailedOfferResponseDTO);
    }

    // Листване на всички оферти за даден потребител
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Offer>> getOffersByClientId(@PathVariable int clientId) {
        List<Offer> offers = offerService.getOffersByClientId(clientId);
        if (offers == null || offers.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(offers);
    }

    // Листване на конкретна оферта
    @GetMapping("/{offerId}")
    public ResponseEntity<Offer> getOfferById(@PathVariable int offerId) {
        Offer offer = offerService.getOfferById(offerId);
        if (offer == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(offer);
    }


    // Изтриване на оферта
    @DeleteMapping("/{offerId}")
    public ResponseEntity<String> deleteOffer(@PathVariable int offerId) {
        boolean isDeleted = offerService.softDeleteOffer(offerId);
        if (isDeleted) {
            return new ResponseEntity<>("Offer with ID " + offerId + " has been softly deleted.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Offer not found with ID " + offerId, HttpStatus.NOT_FOUND);
    }


    // Приемане на оферта - в склучай в които потребителя вземе автомобила
    @PutMapping("/{offerId}/accept")
    public ResponseEntity<Offer> acceptOffer(@PathVariable int offerId) {
        Offer updatedOffer = offerService.acceptOffer(offerId);
        if (updatedOffer == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedOffer, HttpStatus.OK);
    }

}
