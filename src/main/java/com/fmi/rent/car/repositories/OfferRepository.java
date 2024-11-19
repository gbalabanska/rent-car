package com.fmi.rent.car.repositories;

import com.fmi.rent.car.entities.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {

    List<Offer> findByClientId(int clientId);
}