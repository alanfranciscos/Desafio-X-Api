package br.com.desafiox.api.model.sale;

import br.com.desafiox.api.model.client.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


//Entity - Anotação para identificar que a identidade esta mapeada no db
//Table - Especifica o nome da tabela
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vendas")
public class Sale {

    //Id -> Identificar a primary key no db
    //generated -> A estrategia para criação da primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long id;

    //Estabelecer a relação da tabela vendas com a cliente
    //Many to one -> muitos pra um -> posso ter varias vendas para um unico cliente
    //Join Column -> Usado para identificação da chave estrangeira
    @ManyToOne
    @JoinColumn(name = "cliente_cnpj", referencedColumnName = "cnpj")
    private Client cliente;

    //Temporal -> .date -> identifica que só a data irá persistir ao pegar os valores do db.
    //Column -> Identifica que a coluna em questao nao aceita um valor nulo
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date data;

    //Enumerated com os parametros inseridos, indica que o valor a ser persistido no db será uma string
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusVenda status;

    //precision -> 19 -> o maximo suportado | significa que ele pode guardar numeros de até 19 digitos
    //scale -> scale 2 significa que ele terá duas casas decimais
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

}
