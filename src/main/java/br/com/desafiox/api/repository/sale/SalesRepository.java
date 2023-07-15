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
    @Query(value = "DELETE FROM vendas v WHERE v.cliente_cnpj = ?1", nativeQuery = true)
    void deleteAllSalesOfAnClient(String cliente);

    @Query(value = "SELECT COUNT(*) FROM vendas v " +
            "INNER JOIN cliente c " +
            "ON v.cliente_cnpj = c.cnpj " +
            "where c.nome = ?1",
            nativeQuery = true)
    int getTableLength(String client);

    /**
     * Aqui pegamos a soma das vendas realizadas no ANO (Atual)
     */
    @Query(value = "select sum(valor) from vendas v " +
            "where to_char(v.data, 'YYYY') = (SELECT to_char(cast(now() as date),'YYYY'))",
            nativeQuery = true)
    double getSumOfSales();

    /**
     * O cliente com mais vendas no mes do ano atual
     */
    @Query(value = "select nome from cliente c where cnpj = " +
            "(" +
            "   select cliente_cnpj from " +
            "       (" +
            "           select cliente_cnpj, count(cliente_cnpj) as total_vendas " +
            "           from vendas v where to_char(v.data,'YYYY-MM') = (SELECT to_char(cast(now() as date),'YYYY-MM')" +
            "       ) " +
            "   group by cliente_cnpj order by total_vendas desc limit 1" +
            ") as most);", nativeQuery = true)
    String getClientNameWithMostSales();


    /**
     * O cliente com maior faturamento no mes do ano atual
     */
    @Query(value = "SELECT c.nome, n.total_vendas " +
            "FROM cliente c " +
            "JOIN (" +
            "    SELECT cliente_cnpj, SUM(valor) AS total_vendas " +
            "    FROM vendas v " +
            "    WHERE TO_CHAR(v.data, 'YYYY-MM') = TO_CHAR(CAST(NOW() AS date), 'YYYY-MM') " +
            "    GROUP BY cliente_cnpj " +
            "    ORDER BY total_vendas DESC " +
            "    LIMIT 1 " +
            ") AS n ON c.cnpj = n.cliente_cnpj", nativeQuery = true)
    List<Object[]> getClientWithMostBillingInMonth();


    /**
     * O cliente com maior faturamento no ano atual
     */
    @Query(value = "SELECT c.nome, n.total_vendas " +
            "FROM cliente c " +
            "JOIN (" +
            "    SELECT cliente_cnpj, SUM(valor) AS total_vendas " +
            "    FROM vendas v " +
            "    WHERE TO_CHAR(v.data, 'YYYY') = TO_CHAR(CAST(NOW() AS date), 'YYYY') " +
            "    GROUP BY cliente_cnpj " +
            "    ORDER BY total_vendas DESC " +
            "    LIMIT 1 " +
            ") AS n ON c.cnpj = n.cliente_cnpj", nativeQuery = true)
    List<Object[]> getClientWithMostBillingYearly();


    @Query(value = "select TO_CHAR(v.data, 'TMMonth') as mes, count(*) as vendas, sum(valor) as total " +
            "from vendas v WHERE TO_CHAR(v.data, 'YYYY') = TO_CHAR(CAST(NOW() AS date), 'YYYY') " +
            "group by mes", nativeQuery = true)
    List<Object[]> getSalesPerMonth();
}