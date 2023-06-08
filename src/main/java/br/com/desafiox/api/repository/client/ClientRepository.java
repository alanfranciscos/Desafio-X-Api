package br.com.desafiox.api.repository.client;

import br.com.desafiox.api.model.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByCnpj(String cnpj);

    @Modifying
    @Query(value = "DELETE FROM cliente WHERE cnpj = :clientId", nativeQuery = true)
    void deleteClientById(@Param("clientId") String clientId);

}
