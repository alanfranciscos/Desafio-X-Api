package br.com.desafiox.api.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cliente")
public class Client {
    private String nome;

    @Id
    @Column(unique = true)
    private String cnpj;

    private String email;

    private String telefone;

    private String estado;

    private Point location;
}
