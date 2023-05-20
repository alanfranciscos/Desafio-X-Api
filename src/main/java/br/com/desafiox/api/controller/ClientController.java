package br.com.desafiox.api.controller;

import br.com.desafiox.api.model.Client;
import br.com.desafiox.api.model.State;
import br.com.desafiox.api.repository.ClientRepository;
import br.com.desafiox.api.service.ClientService;
import br.com.desafiox.api.service.IBGEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    private final IBGEService ibgeService;
    private final ClientRepository clientRepository;

    @Autowired
    public ClientController(ClientService clientService, IBGEService ibgeService, ClientRepository clientRepository) {
        this.clientService = clientService;
        this.ibgeService = ibgeService;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/states")
    public State[] getStates() {
        return ibgeService.getStates();
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        if(getClientById(client.getCnpj()) == null) {
            for(State s : ibgeService.getStates()) {
                if(client.getEstado().equals(s.getSigla())) {
                    return ResponseEntity.ok(clientService.createClient(client));
                }
            }
        }
        //422 Este código de status descreve que o servidor compreende a solicitação, que não há erro na sintaxe mas não pode processá-la
        return ResponseEntity.unprocessableEntity().build();
    }

    @GetMapping
    public Page<Client> getClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "nome") String sortColumn
    ) {
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(sortColumn));
        return clientRepository.findAll(pageRequest);
    }

    @GetMapping("/{id}")
    public Client getClientById(@PathVariable String id) {
        return clientService.getClientById(id);
    }

    @PutMapping("/{id}")
    public Client updateClient(@PathVariable Long id, @RequestBody Client client) {
        return clientService.updateClient(id, client);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
    }
}

