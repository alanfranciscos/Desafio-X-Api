package br.com.desafiox.api.controller;

import br.com.desafiox.api.model.State;
import br.com.desafiox.api.service.IBGEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/states")
public class IBGEController {
    private final IBGEService ibgeService;

    @Autowired
    public IBGEController(IBGEService ibgeService) {
        this.ibgeService = ibgeService;
    }

    @GetMapping
    public State[] getStates() {
        return ibgeService.getStates();
    }

    @GetMapping("/{id}/metadados")
    public String getEstadoMetadados(@PathVariable String id) {
        return ibgeService.getEstadoMetadados(id);
    }
}
