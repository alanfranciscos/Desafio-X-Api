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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    /**
     * Matodo utilizado para criar um cliente.
     *
     * @param client Para se criar um cliente, é necessário passar como parâmetro um cliente.
     * @return o status do mesmo, se obteve sucesso ou não.
     */
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        //Verifica se o usuário com o cnpj já foi cadastrado (validação extra)
        if (clientRepository.findByCnpj(client.getCnpj()) == null) {
            String formatedId = client.getCnpj().replaceAll(
                    "^(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})$",
                    "$1.$2.$3/$4-$5"
            );
            client.setCnpj(formatedId);

            for (State s : ibgeService.getStates()) {
                if (client.getEstado().equals(s.getId())) {
                    return ResponseEntity.ok(clientService.createClient(client));
                }
            }
        }
        //422 Este código de status descreve que o servidor compreende a solicitação, que não há erro na sintaxe mas não pode processá-la
        return ResponseEntity.unprocessableEntity().build();
    }

    /**
     * Matodo utilizado para editar o cliente.
     *
     * @param id     é necessário para podermos acessar o cliente e caso o usuário tenha alterado o cnpj, não quebre no final.
     * @param client é necessário passar como parâmetro o cliente com as novas informações
     * @return O cliente caso obtenha sucesso ou erro, caso obtenhamos erro
     */
    @PutMapping("/{id}")
    public Client updateClient(@PathVariable String id, @RequestBody Client client) {
        String formatedId = id.replaceAll(
                "^(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})$",
                "$1.$2.$3/$4-$5"
        );
        return clientService.updateClient(formatedId, client);
    }

    /**
     * Metodo utilizado para deletar cliente
     *
     * @param id o id do cliente que se deseja deletar (cnpj)
     * @return se a requisição obteve sucesso ou não
     */
    @DeleteMapping
    @Transactional
    public ResponseEntity<Client> deleteClient(@RequestParam String id) {
        String formatedId = id.replaceAll(
                "^(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})$",
                "$1.$2.$3/$4-$5"
        );
        if (clientService.getClientById(formatedId) != null) {
            salesRepository.deleteAllSalesOfAnClient(formatedId);
            clientService.deleteClient(formatedId);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Faz uma requisição para retornar todos os clientes paginados
     *
     * @param page       parametro para acessar a pagina
     * @param sortColumn A coluna pela qual esta se ordenando os dados
     * @param sortOrder  DESC OU ASC (decrescente ou crescente) referente a coluna ordenada
     * @return retorna um objeto onde é possível acessar tabto os dados em questão, quanto o numero de elementos e paginas
     */
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

    /**
     * Utilizado para pegar o cliente por cnpj com digitos, assim pegando apenas os dados do cliente especifio
     *
     * @param id o cnpj do cliente desejado
     * @return o cliente caso exista
     */
    @GetMapping("/{id}")
    public Client getClientById(@PathVariable String id) {
        String formatedId = id.replaceAll(
                "^(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})$",
                "$1.$2.$3/$4-$5"
        );
        return clientService.getClientById(formatedId);
    }

    /**
     * Faz uma requisição para retornar todos os clientes paginados baseado no que foi pesquisado
     *
     * @param search     parametro utilizado para procurar tanto pelo nome do cliente, quanto pelo cnpj
     * @param page       parametro para acessar a pagina
     * @param sortColumn A coluna pela qual esta se ordenando os dados
     * @param sortOrder  DESC OU ASC (decrescente ou crescente) referente a coluna ordenada
     * @return retorna um objeto onde é possível acessar tabto os dados em questão, quanto o numero de elementos e paginas
     */
    @GetMapping("/filtered")
    public Map<String, Object> getClientBySearch(@RequestParam String search,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "nome") String sortColumn,
                                                 @RequestParam(defaultValue = "asc") String sortOrder) {
        return clientService.getClientByNameOrCnpj(search, page, sortColumn, sortOrder);
    }


    /**
     * Método utilizado para pegar apenas os nomes e cnpj's dos cliesntes,
     * para não necessitar fazer uma requisição com todos os dados
     * e assim gerar um custo desnecessario de processamento
     *
     * @return uma lista com objetoc contento nome e cnpj dos clientes ja em formato para o
     * select do front
     */
    @GetMapping("/get-cnpj-and-names")
    public List<HashMap<String, String>> getCnpjAndNames() {
        return clientService.getCnpjAndNames();
    }


}




