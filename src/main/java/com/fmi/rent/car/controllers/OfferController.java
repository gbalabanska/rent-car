package com.fmi.rent.car.controllers;

import com.fmi.rent.car.dto.CreateOfferRequestBodyDTO;
import com.fmi.rent.car.dto.CreateOfferResponseBodyDTO;
import com.fmi.rent.car.entities.Offer;
import com.fmi.rent.car.services.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offers")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateOfferResponseBodyDTO> createOffer(@RequestBody CreateOfferRequestBodyDTO request) {
        CreateOfferResponseBodyDTO detailedOfferResponseDTO = offerService.createOffer(request);
        return ResponseEntity.ok(detailedOfferResponseDTO);
    }

    // //листване на всички оферти за даден потребител
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Offer>> getOffersByClientId(@PathVariable int clientId) {
        List<Offer> offers = offerService.getOffersByClientId(clientId);
        return ResponseEntity.ok(offers);
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<Offer> getOfferById(@PathVariable int offerId) {
        Offer offer = offerService.getOfferById(offerId);
        return ResponseEntity.ok(offer);
    }

    @DeleteMapping("/{offerId}")
    public ResponseEntity<Void> deleteOffer(@PathVariable int offerId) {
        offerService.deleteOffer(offerId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{offerId}/accept")
    public ResponseEntity<Offer> acceptOffer(@PathVariable int offerId) {
        Offer updatedOffer = offerService.acceptOffer(offerId);
        return ResponseEntity.ok(updatedOffer);
    }

}
