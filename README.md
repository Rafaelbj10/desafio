# 📍 Desafio CEP — Integração ViaCEP com Spring Boot

API REST desenvolvida em Java com Spring Boot para consulta de endereços a partir de um CEP, consumindo a API pública [ViaCEP](https://viacep.com.br).

---

## 🚀 Tecnologias

* Java 21
* Spring Boot 3.2.5
* Spring Cloud OpenFeign 4.1.1
* SpringDoc OpenAPI (Swagger) 2.5.0
* Lombok
* Maven
* Docker
* LocalStack (SQS)
* WireMock

---

## 📁 Estrutura do Projeto

```
src/main/java/com/desafio/
├── controller/          # Endpoints REST
├── infra/
│   ├── client/          # Feign Client (ViaCEP)
│   │   └── response/    # DTOs de resposta
├── service/             # Regras de negócio
└── DesafioApplication   # Classe principal
```

---

## 📋 Pré-requisitos

* Java 21+
* Maven 3.8+
* Docker + Docker Compose

---

## ⚙️ Configuração

O projeto utiliza perfis do Spring para separar os ambientes:

* **`application.yml`** → configurações base (produção)
* **`application-dev.yml`** → desenvolvimento local

> 💡 No profile `dev`, a API ViaCEP é mockada via WireMock (`localhost:8089`).
> Em `prod`, a aplicação consome a API real.

---

## 🐳 Como Rodar

### 1. Subir os serviços com Docker

O projeto depende de **MySQL**, **WireMock** e **LocalStack (SQS)**:

```bash
docker-compose up -d
```

---

## 📦 Serviços disponíveis após o comando

| Serviço    | Container     | Porta | Descrição                            |
| ---------- | ------------- | ----- | ------------------------------------ |
| MySQL      | mysql-desafio | 3306  | Banco de dados da aplicação          |
| WireMock   | wiremock      | 8089  | Mock da API ViaCEP                   |
| LocalStack | localstack    | 4566  | Emulador AWS com SQS para mensageria |

## Cria a tabela no MySQL
Crie a tabela `cep_log` em sua IDE de banco para armazenar os logs de consulta:
````
create database desafio;

create table cep_log
(
    id            bigint auto_increment
        primary key,
    cep           varchar(255) null,
    response      text         null,
    data_consulta datetime     null
);
````

## 📬 Criar fila SQS (LocalStack)
**No terminal do intellij ou outro terminal, execute:**
aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name cep-queue

## Consultar mensagens na fila SQS
**No terminal do intellij ou outro terminal, execute:**
aws --endpoint-url=http://localhost:4566 sqs receive-message \
--queue-url http://localhost:4566/000000000000/cep-queue

## 🔎 Verificar containers

```bash
docker-compose ps
```

### 🛑 Parar containers

```bash
docker-compose down
```

---

## ▶️ Executando a Aplicação

### Desenvolvimento

```bash
mvn spring-boot:run
```

### IntelliJ

Vá em:

```
Run → Edit Configurations → Active profiles
```

Use:

* `dev` → usa WireMock
* `prod` → usa ViaCEP real

---

## 📡 Endpoints

### Consultar CEP

```
GET /cep/{cep}
```

### Exemplo

```
GET http://localhost:8081/cep/01310100
```

### Resposta

```json
{
  "cep": "01310100",
  "logradouro": "Avenida Paulista",
  "complemento": "de 610 a 1110 - lado par",
  "bairro": "Bela Vista",
  "localidade": "São Paulo",
  "uf": "SP"
}
```

---

## ⚠️ Erros possíveis

| Status | Descrição                 |
| ------ | ------------------------- |
| 400    | CEP inválido ou em branco |
| 404    | CEP não encontrado        |

---

## 📘 Swagger

Acesse:

```
http://localhost:8081/swagger-ui/index.html
```

---

## 🧠 Decisões Técnicas

### 🔹 Feign Client

Consumo declarativo da API ViaCEP:

```java
@FeignClient(name = "viaCepClient", url = "${via-cep.url}")
public interface ViaCepClient {
    @GetMapping("/{cep}/json/")
    CepResponse buscarCep(@PathVariable("cep") String cep);
}
```

---

### 🔹 Validação no Service

Evita chamadas desnecessárias para APIs externas.

---

### 🔹 Perfis de Ambiente

Separação entre `dev` e `prod` sem alterar código.

---

### 🔹 Mensageria com SQS (LocalStack)

A aplicação pode enviar eventos para uma fila SQS simulada via LocalStack.

**Endpoint padrão:**

```
http://localhost:4566
```

---

## 🧪 Testes

Uso de WireMock para garantir isolamento:

```bash
mvn test
```
