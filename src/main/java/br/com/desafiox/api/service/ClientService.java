package br.com.desafiox.api.service;

import br.com.desafiox.api.model.Client;
import br.com.desafiox.api.repository.ClientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final IBGEService ibgeService;

    @Autowired
    public ClientService(ClientRepository clientRepository,  IBGEService ibgeService) {
        this.clientRepository = clientRepository;
        this.ibgeService = ibgeService;
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(String id) {
        return clientRepository.findByCnpj(id);
    }

    public Client updateClient(Long id, Client client) {
        Client existingClient = clientRepository.findById(id).orElse(null);
        if (existingClient != null) {
            existingClient.setNome(client.getNome());
            existingClient.setCnpj(client.getCnpj());
            existingClient.setEmail(client.getEmail());
            existingClient.setTelefone(client.getTelefone());
            existingClient.setEstado(client.getEstado());
            existingClient.setLocation(client.getLocation());
            return clientRepository.save(existingClient);
        }
        return null;
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
