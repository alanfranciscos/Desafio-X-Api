package br.com.desafiox.api.repository.sale;

import br.com.desafiox.api.model.sale.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesRepository extends JpaRepository<Sale, Long> {
    Sale findSaleById(Long id);

    @Modifying
    @Query(value = "DELETE FROM vendas WHERE id = :saleID", nativeQuery = true)
    void deleteSaleById(@Param("saleID") Long saleID);

    @Modifying
    @Query(value = "DELETE FROM vendas v WHERE v.cliente_cnpj = ?1", nativeQuery = true)
    void deleteAllSalesOfAnClient(String cliente);

    @Query(value = "SELECT COUNT(*) FROM vendas v " +
            "INNER JOIN cliente c " +
            "ON v.cliente_cnpj = c.cnpj " +
            "where c.nome = ?1",
            nativeQuery = true)
    int getTableLength(String client);
}