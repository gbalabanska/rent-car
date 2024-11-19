package com.fmi.rent.car.services;

import com.fmi.rent.car.entities.Client;
import com.fmi.rent.car.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client registerClient(Client client) {
        return clientRepository.save(client);
    }
}
