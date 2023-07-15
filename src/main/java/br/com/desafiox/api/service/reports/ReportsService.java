package br.com.desafiox.api.service.reports;

import br.com.desafiox.api.model.client.Client;
import br.com.desafiox.api.repository.client.ClientRepository;
import br.com.desafiox.api.repository.sale.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ReportsService {
    private final ClientRepository clientRepository;
    private final SalesRepository salesRepository;

    @Autowired
    public ReportsService(ClientRepository clientRepository, SalesRepository salesRepository) {
        this.clientRepository = clientRepository;
        this.salesRepository = salesRepository;
    }


    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    public HashMap<String, HashMap<String, Object>> getCards() {
        HashMap<String, HashMap<String, Object>> result = new HashMap<>();
        String somaDasVendas = String.valueOf(salesRepository.getSumOfSales());
        String clienteComMaiorNumeroDeVendas = salesRepository.getClientNameWithMostSales();

        HashMap<String, Object> item = new HashMap<>();
        item.put("valor", Double.valueOf(somaDasVendas));
        result.put("soma_das_vendas_anual", item);

        item = new HashMap<>();
        item.put("valor", clienteComMaiorNumeroDeVendas);
        result.put("cliente_com_maior_vendas_mensal", item);

        item = new HashMap<>();
        for (Object[] row : salesRepository.getClientWithMostBillingInMonth()) {
            item.put("cliente", row[0]);
            item.put("valor", row[1]);
        }
        result.put("cliente_com_maior_faturamento_mensal", item);

        item = new HashMap<>();
        for (Object[] row : salesRepository.getClientWithMostBillingYearly()) {
            item.put("cliente", row[0]);
            item.put("valor", row[1]);
        }
        result.put("cliente_com_maior_faturamento_anual", item);
        return result;
    }

    public ArrayList<HashMap<String, Object>> getSalesPerMonth() {
        ArrayList<HashMap<String, Object>> result = new ArrayList<>();
        for (Object[] row : salesRepository.getSalesPerMonth()) {
            HashMap<String, Object> rowKeys = new HashMap<>();
            rowKeys.put("mes", row[0]);
            rowKeys.put("vendas", row[1]);
            rowKeys.put("total", row[2]);
            result.add(rowKeys);
        }
        return result;
    }

    public String getSalesPerMonthDownloader() {
        String result;
        result = "Mês;Vendas;Total\n";
        for (Object[] row : salesRepository.getSalesPerMonth()) {
            result += row[0] + ";" + row[1] + ";" + row[2] + "\n";
        }
        return result.trim();
    }

}
