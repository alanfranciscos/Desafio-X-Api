package br.com.desafiox.api.controller.sales;


import br.com.desafiox.api.model.sale.Sale;
import br.com.desafiox.api.repository.sale.SalesRepository;
import br.com.desafiox.api.service.sale.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/sales")
public class SalesController {
    private final SalesService salesService;

    @Autowired
    public SalesController(SalesService salesService, SalesRepository salesRepository) {
        this.salesService = salesService;
    }

    @GetMapping
    public Page<Sale> getSale(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "id") String sortColumn,
                              @RequestParam(defaultValue = "asc") String sortOrder) {
        return salesService.getSale(page, sortColumn, sortOrder);
    }

    @GetMapping("/getPerId")
    public Map<String, Object> getSaleByClient(
            @RequestParam String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "cliente_cnpj") String sortColumn,
            @RequestParam(defaultValue = "DESC") String sortOrder) {
        return salesService.getSaleByClient(search, page, sortColumn, sortOrder);
    }

    @GetMapping("/status")
    public List<HashMap<String, String>> returnAllStatusSale() {
        return salesService.returnAllStatusSale();
    }


    @PostMapping
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale) {
        return ResponseEntity.ok(salesService.createSale(sale));
    }

    @PutMapping
    public Sale editSale(Long id, Sale sale) {
        return salesService.editSale(id, sale);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Sale> deleteSale(@PathVariable Long id) {
        return salesService.deleteByID(id);
    }
}
