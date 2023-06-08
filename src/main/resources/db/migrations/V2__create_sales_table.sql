CREATE TABLE vendas (
    id SERIAL PRIMARY KEY,
    cliente_cnpj VARCHAR(14) NOT NULL,
    data DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (cliente_cnpj) REFERENCES clientes(cnpj) ON DELETE CASCADE
 );