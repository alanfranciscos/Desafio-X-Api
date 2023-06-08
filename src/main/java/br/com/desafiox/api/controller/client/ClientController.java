package br.com.desafiox.api.controller.client;

import br.com.desafiox.api.model.client.Client;
import br.com.desafiox.api.model.ibge.State;
import br.com.desafiox.api.repository.client.ClientRepository;
import br.com.desafiox.api.repository.sale.SalesRepository;
import br.com.desafiox.api.service.client.ClientService;
import br.com.desafiox.api.service.ibge.IBGEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;
    private final SalesRepository salesRepository;
    private final ClientRepository clientRepository;
    private final IBGEService ibgeService;

    @Autowired
    public ClientController(ClientService clientService, SalesRepository salesRepository, IBGEService ibgeService, ClientRepository clientRepository) {
        this.clientService = clientService;
        this.salesRepository = salesRepository;
        this.clientRepository = clientRepository;
        this.ibgeService = ibgeService;
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        //Verifica se o usuário com o cnpj já foi cadastrado (validação extra)
        if (getClientById(client.getCnpj()) == null) {
            for (State s : ibgeService.getStates()) {
                if (client.getEstado().equals(s.getId())) {
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
            @RequestParam(defaultValue = "nome") String sortColumn,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(direction, sortColumn));
        return clientRepository.findAll(pageRequest);
    }

    @GetMapping("/{id}")
    public Client getClientById(@PathVariable String id) {
        String formatedId = id.replaceAll(
                "^(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})$",
                "$1.$2.$3/$4-$5"
        );
        return clientService.getClientById(formatedId);
    }

    @PutMapping("/{id}")
    public Client updateClient(@PathVariable String id, @RequestBody Client client) {
        String formatedId = id.replaceAll(
                "^(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})$",
                "$1.$2.$3/$4-$5"
        );
        return clientService.updateClient(formatedId, client);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Client> deleteClient(@PathVariable String id) {
        if (getClientById(id) != null) {
            String formatedId = id.replaceAll(
                    "^(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})$",
                    "$1.$2.$3/$4-$5"
            );
            salesRepository.deleteAllSalesOfAnClient(formatedId);
            clientService.deleteClient(formatedId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}

