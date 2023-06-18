package br.com.desafiox.api.service.client;

import br.com.desafiox.api.model.client.Client;
import br.com.desafiox.api.repository.client.ClientRepository;
import br.com.desafiox.api.service.ibge.IBGEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final IBGEService ibgeService;

    @Autowired
    public ClientService(ClientRepository clientRepository, IBGEService ibgeService) {
        this.clientRepository = clientRepository;
        this.ibgeService = ibgeService;
    }

    public List<HashMap<String, String>> getCnpjAndNames() {
        List<Client> clients = clientRepository.findAll();
        List<HashMap<String, String>> result = new ArrayList<>();
        for (Client c : clients) {
            HashMap<String, String> variable = new HashMap<>();
            variable.put("label", c.getNome());
            variable.put("value", c.getCnpj());
            result.add(variable);
        }

        return result;
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

    public Client updateClient(String id, Client client) {
        Client existingClient = clientRepository.findByCnpj(id);
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

    @Transactional
    public void deleteClient(String id) {
        clientRepository.deleteClientById(id);
    }
}
