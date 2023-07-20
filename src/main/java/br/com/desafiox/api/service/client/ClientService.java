package br.com.desafiox.api.service.client;

import br.com.desafiox.api.model.client.Client;
import br.com.desafiox.api.repository.client.ClientRepository;
import br.com.desafiox.api.service.ibge.IBGEService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * @param search     parametro utilizado para procurar tanto pelo nome do cliente, quanto pelo cnpj
     * @param page       parametro para acessar a pagina
     * @param sortColumn A coluna pela qual esta se ordenando os dados
     * @param sortOrder  DESC OU ASC (decrescente ou crescente) referente a coluna ordenada
     * @return retorna um objeto no qual é possível acessar tabto os dados em questão, quanto o numero de elementos e paginas
     */
    public Map<String, Object> getClientByNameOrCnpj(@NotNull String search,
                                                     @NotNull int page,
                                                     @NotNull String sortColumn,
                                                     @NotNull String sortOrder) {
        Map<String, Object> returnList = new HashMap<>();
        List<Client> itens = clientRepository.getClientByNameOrCnpj(search, sortColumn, sortOrder.toUpperCase(), page);

        int lengthItens = itens.size();
        int totalPages = (lengthItens / 10) + 1;

        returnList.put("content", itens);
        returnList.put("totalElements", lengthItens);
        returnList.put("totalPages", totalPages);

        return returnList;
    }

}
