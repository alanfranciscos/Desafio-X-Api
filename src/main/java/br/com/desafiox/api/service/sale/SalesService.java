package br.com.desafiox.api.service.sale;

import br.com.desafiox.api.model.sale.Sale;
import br.com.desafiox.api.model.sale.StatusVenda;
import br.com.desafiox.api.repository.sale.SalesRepository;
import br.com.desafiox.api.service.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SalesService {
    private final SalesRepository salesRepository;
    private final ClientService clientService;

    @Autowired
    public SalesService(SalesRepository salesRepository, ClientService clientService) {
        this.salesRepository = salesRepository;
        this.clientService = clientService;
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

    public Sale getSaleByID(long id) {
        return salesRepository.findSaleById(id);
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
        if (getSaleByID(id) != null) {
            salesRepository.deleteSaleById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    public StatusVenda[] returnAllStatusSale() {
        return StatusVenda.values();
    }
}
