# 📮 Desafio CEP — Integração ViaCEP com Spring Boot

API REST desenvolvida em Java com Spring Boot para consulta de endereços a partir de um CEP, consumindo a API pública [ViaCEP](https://viacep.com.br).

---

## 🚀 Tecnologias

- Java 21
- Spring Boot 3.2.5
- Spring Cloud OpenFeign 4.1.1
- SpringDoc OpenAPI (Swagger) 2.5.0
- Lombok
- Maven

---

## 📁 Estrutura do Projeto

```
src/main/java/com/desafio/
├── controller/          # Endpoints REST
├── infra/
│   └── client/          # Feign Client (ViaCEP)
│       └── response/    # DTOs de resposta
├── service/             # Regras de negócio
└── DesafioApplication   # Classe principal
```

---

## ⚙️ Pré-requisitos

- Java 21+
- Maven 3.8+

---

## 🔧 Configuração

O projeto utiliza perfis do Spring para separar os ambientes. As configurações base ficam em `application.yml` e as específicas de cada ambiente em arquivos separados.

**`application.yml`** — configurações base (produção)

**`application-dev.yml`** — sobrescreve configurações para desenvolvimento local


> 💡 A principal diferença entre os perfis é a URL do ViaCEP: em `dev` aponta para o WireMock local (`localhost:8089`), em produção aponta para a API real.

---

## ▶️ Como Rodar

### 1. Subir os serviços com Docker

O projeto depende do **MySQL** e do **WireMock** para rodar localmente. Suba os containers antes de iniciar a aplicação:

```bash
docker-compose up -d
```

Serviços disponíveis após o comando:

| Serviço  | Container       | Porta  | Descrição                          |
|----------|-----------------|--------|------------------------------------|
| MySQL    | mysql-desafio   | 3306   | Banco de dados da aplicação        |
| WireMock | wiremock        | 8089   | Mock da API ViaCEP para dev/testes |

Para verificar se os containers estão rodando:
```bash
docker-compose ps
```

Para parar os containers:
```bash
docker-compose down
```


### Desenvolvimento
```bash
mvn spring-boot:run
```

### Pelo IntelliJ
Vá em **Run → Edit Configurations → Active profiles** e informe `dev` ou `prod`. Sendo `prod` uma simulação de um ambiente real, a aplicação irá consumir a API ViaCEP diretamente.

---

## 📌 Endpoints

### Consultar CEP

```
GET /cep/{cep}
```

**Exemplo de requisição:**
```
GET http://localhost:8081/cep/01310100
```

**Exemplo de resposta:**
```json
{
  "cep": "01310100",
  "logradouro": "Avenida Paulista",
  "complemento": "de 610 a 1Download110 - lado par",
  "bairro": "Bela Vista",
  "localidade": "São Paulo",
  "uf": "SP"
}
```

**Erros possíveis:**

| Status | Descrição |
|--------|-----------|
| 400 | CEP inválido ou em branco |
| 404 | CEP não encontrado |

---

## 📖 Documentação Swagger

Com a aplicação rodando, acesse:

```
http://localhost:8081/swagger-ui/index.html
```

---

## 🧩 Decisões Técnicas

### Feign Client
Utilizado para consumir a API ViaCEP de forma declarativa, sem necessidade de `RestTemplate` ou `WebClient` manual.

```java
@FeignClient(name = "viaCepClient", url = "${via-cep.url}")
public interface ViaCepClient {
    @GetMapping("/{cep}/json/")
    CepResponse buscarCep(@PathVariable("cep") String cep);
}
```

### Validação no Service
A validação do CEP é feita antes da chamada externa, evitando requisições desnecessárias à ViaCEP.

### Perfis de Ambiente
Separação entre `dev` e `prod` via Spring Profiles, permitindo configurações distintas por ambiente sem alteração de código.

---

## 🧪 Testes

O projeto utiliza **WireMock** para mockar a API ViaCEP nos testes, garantindo isolamento e independência de rede.

```bash
mvn test
```