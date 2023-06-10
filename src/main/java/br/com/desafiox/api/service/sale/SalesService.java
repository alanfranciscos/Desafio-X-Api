package br.com.desafiox.api.service.sale;

import br.com.desafiox.api.model.client.Client;
import br.com.desafiox.api.model.sale.Sale;
import br.com.desafiox.api.model.sale.StatusVenda;
import br.com.desafiox.api.repository.sale.SalesRepository;
import br.com.desafiox.api.service.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;

@Service
public class SalesService {
    private final SalesRepository salesRepository;
    private final ClientService clientService;
    private final EntityManager entityManager;

    @Autowired
    public SalesService(SalesRepository salesRepository, ClientService clientService, EntityManager entityManager) {
        this.salesRepository = salesRepository;
        this.clientService = clientService;
        this.entityManager = entityManager;
    }

    public Sale createSale(Sale sale) {
        return salesRepository.save(sale);
    }

    public Page<Sale> getSale(int page, String sortColumn, String sortOrder) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, 10, Sort.by(direction,
                sortColumn.equalsIgnoreCase("cliente") ? "cliente.nome" : sortColumn));
        return salesRepository.findAll(pageRequest);
    }


    public Map<String, Object> getSaleByClient(String search, int page, String sortColumn, String sortOrder) {
        Map<String, Object> returnList = new HashMap<>();

        //Como o cnpj esta como chave primaria e desejamos ordenar pelo nome, pegamos apenas a coluna nome
        String sortColumnFormated = sortColumn.toLowerCase();
        if (sortColumnFormated.equals("cliente")) {
            sortColumnFormated = "cliente_cnpj.nome";
        }

        List<Sale> pageableSales = new ArrayList<>();

        //Foi necessario criar uma query externa ao repository, mesmo isso nao sendo uma boa prática
        //Isso porque quando passavamos por parametro, a query retornava '?' ao invés do parametro inserido
        Query query = entityManager.createNativeQuery(
                "SELECT id, data, status, valor, nome, cnpj, email, telefone, estado, location " +
                        "FROM vendas v INNER JOIN cliente c " +
                        "ON v.cliente_cnpj = c.cnpj " +
                        "WHERE c.nome = '" + search + "' " +
                        "ORDER BY " + sortColumnFormated + " " + sortOrder + " " +
                        "LIMIT 10 OFFSET " + page
        );


        List<Object[]> result = query.getResultList();

        for (Object[] row : result) {
            Sale sale = new Sale();
//             0=id, 1=data, 2=status,
//             3=valor,
//             cliente -->
//             4=nome, 5=cnpj,
//             6=email, 7=telefone, 8=estado,
//             9=location

            sale.setId(Long.valueOf(row[0].toString()));
            sale.setData((Date) row[1]);
            sale.setStatus(StatusVenda.valueOf((String) row[2]));
            sale.setValor((BigDecimal) row[3]);

            // Não consegui pegar os dados de localizaçao do cliente, mas como nao
            //esta sendo utilizado, resolvi colocar valores = 0 para que o codigo nao quebre

            Client client = new Client();
            Point point = new Point(0, 0);
            client.setNome((String) row[4]);
            client.setCnpj((String) row[5]);
            client.setEmail((String) row[6]);
            client.setTelefone((String) row[7]);
            client.setEstado((String) row[8]);
            client.setLocation(point);

            sale.setCliente(client);
            pageableSales.add(sale);
        }


        int lengthItens = salesRepository.getTableLength(search);
        int totalPages = (lengthItens / 10) + 1;

        //Para manter o padrão em que retonra no cliente, fiz dessa forma
        returnList.put("content", pageableSales);
        returnList.put("totalElements", lengthItens);
        returnList.put("totalPages", totalPages);
        return returnList;
    }

    public Sale editSale(Long id, Sale sale) {
        Sale existingSale = salesRepository.findSaleById(id);
        if (existingSale != null) {
            existingSale.setCliente(sale.getCliente());
            existingSale.setData(sale.getData());
            existingSale.setValor(sale.getValor());
            existingSale.setData(sale.getData());
            return salesRepository.save(existingSale);
        }
        return null;
    }

    public ResponseEntity<Sale> deleteByID(Long id) {
        salesRepository.deleteSaleById(id);
        return ResponseEntity.ok().build();
    }


    public StatusVenda[] returnAllStatusSale() {
        return StatusVenda.values();
    }
}
