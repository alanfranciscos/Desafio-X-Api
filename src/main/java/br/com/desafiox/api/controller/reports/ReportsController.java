package br.com.desafiox.api.controller.reports;

import br.com.desafiox.api.model.client.Client;
import br.com.desafiox.api.service.reports.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/reports")
public class ReportsController {
    private final ReportsService reportsService;

    @Autowired
    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @GetMapping("/clients")
    public List<Client> getClients() {
        return reportsService.getClients();
    }

    @GetMapping("/cards")
    public HashMap getCards() {
        return reportsService.getCards();
    }

    @GetMapping("/sales")
    public ArrayList getSalesPerMonth() {
        return reportsService.getSalesPerMonth();
    }

    @GetMapping("/sales/download-csv")
    public String getSalesPerMonthDownloader() {
        return reportsService.getSalesPerMonthDownloader();
    }
}
