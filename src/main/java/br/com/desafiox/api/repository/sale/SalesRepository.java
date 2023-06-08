package br.com.desafiox.api.repository.sale;

import br.com.desafiox.api.model.sale.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<Sale, Long> {
    Sale findSaleById(Long id);

    @Modifying
    @Query(value = "DELETE FROM vendas WHERE id = :saleID", nativeQuery = true)
    void deleteSaleById(@Param("saleID") Long saleID);

    @Modifying
    @Query(value = "DELETE * FROM vendas v " +
            "INNER JOIN cliente c " +
            "ON v.cliente_cnpj = c.cnpj " +
            "WHERE c.cnpj = :cliente",
            nativeQuery = true)
    List<Sale> deleteAllSalesOfAnClient(@Param("cliente") String cliente);
}