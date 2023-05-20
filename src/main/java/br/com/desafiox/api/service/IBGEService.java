package br.com.desafiox.api.service;

import br.com.desafiox.api.model.State;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IBGEService {

    private static final String IBGE_API_URL = "https://servicodados.ibge.gov.br/api/v1/localidades/estados";

    public State[] getStates() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<State[]> response = restTemplate.getForEntity(IBGE_API_URL, State[].class);
        return response.getBody();
    }
}

