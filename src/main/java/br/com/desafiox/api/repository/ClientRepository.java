package br.com.desafiox.api.repository;

import br.com.desafiox.api.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByCnpj(String cnpj);
}
