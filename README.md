# Desafio-X-Api

# Bibliotecas utilizadas
- Lombok
- Flyway Core
- PostgreSQL JDBC Drive

# Sobre o banco de dados
- É necessário ter o postgrees na versão 14 instalado.
- É necessário que tenha o geometries instalado.

# Sobre a instalação
- É necesário abrir o programa em um editor de texto(Indico intellij para backend em java)
- Espere carregar as pedendcias contidas no arquivo pom.xml (Maven)
- Configure seu banco de dados como descrito no arquivo de configuração ou altere o mesmo.
- De o "Run" no arquivo ApiApplication.java


# Possíveis problemas na API
Pode ser que talvez você tenha problemas ao rodar a api devido a um problema de certificado. Para solucioná-lo, siga os seguintes passos:

- Certifique de ter configurado a variael de ambiente do java;
- Acesse o site : https://servicodados.ibge.gov.br/api/docs
 Clique no cadeado e em seguida em "a conexão é segura".
 ![image](https://github.com/alanfranciscos/Desafio-X-Api/assets/74225176/fb1ef632-071b-4532-81df-2162a51a9468)
 Clique em o certificado é valido.
 ![image](https://github.com/alanfranciscos/Desafio-X-Api/assets/74225176/08500d98-38f3-40b7-b2e9-8588d97662af)
 Vá em detalhes e exporte o arquivo como um arquivo crt
 ![image](https://github.com/alanfranciscos/Desafio-X-Api/assets/74225176/568e8be8-137d-4b5b-9747-bd19dd2f33dc)
 
- Abra o terminal como adm(Kernel) e execute o seguinte comando:
  `keytool -importcert -trustcacerts -alias <nome_que_sera_salvo> -file <path/nome_do_arquivo) -keystore <path de onde esta localizado a sua keystore> -storepass changeit -noprompt`

  Exemplo:
  `keytool -importcert -trustcacerts -alias ibge_cert -file "C:\ibge_cert.crt" -keystore "C:\Program Files\Java\jdk-17\lib\security\cacerts" -storepass changeit -noprompt`
 
