package com.fmi.rent.car.repositories;

import com.fmi.rent.car.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findByIdAndIsDeletedFalse(int id);
}