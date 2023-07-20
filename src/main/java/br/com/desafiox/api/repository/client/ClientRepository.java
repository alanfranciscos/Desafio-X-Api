package br.com.desafiox.api.repository.client;

import br.com.desafiox.api.model.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByCnpj(String cnpj);

    @Modifying
    @Query(value = "DELETE FROM cliente WHERE cnpj = :clientId", nativeQuery = true)
    void deleteClientById(@Param("clientId") String clientId);

    @Query(value = "SELECT * " +
            "FROM cliente c " +
            "WHERE UPPER(c.cnpj) LIKE UPPER('%' || :search || '%') " +
            "OR UPPER(c.nome) LIKE UPPER('%' || :search || '%') " +
            "ORDER BY " +
            "   CASE  " +
            "       WHEN :sortOrder = 'ASC' THEN :order END ASC, " +
            "   CASE  " +
            "       WHEN :sortOrder = 'DESC' THEN :order END DESC " +
            "LIMIT 10 OFFSET :page", nativeQuery = true)
    List<Client> getClientByNameOrCnpj(
            @Param("search") String search,
            @Param("order") String order,
            @Param("sortOrder") String sortOrder,
            @Param("page") int page
    );
}
