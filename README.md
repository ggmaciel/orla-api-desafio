# Orla Tech API

## Pré-requisitos
- Java 21
- Docker
- Maven

## Descrição
API criada para desafio técnico da Orla Tech.

A aplicação utiliza PostgreSQL como sistema de gerenciamento de banco de dados e Flyway para rodar migrations.

## Instruções de execução

### Executar o projeto localmente
1. Iniciar o banco de dados: `docker compose -f ./docker-compose-db.yml up -d`
2. Compilar o projeto: `mvn clean -B -T 16C package`
3. Executar a aplicação: `java -Dserver.port=8080 -XX:MaxRAMPercentage=70 -jar ./target/orla-api.jar`
4. Remover o banco de dados (Quando finalizar o uso): `docker compose -f ./docker-compose-db.yml down`

### Executar com Docker (Banco de dados e aplicação)
1. Compilar o projeto: `mvn clean -B -T 16C package`
2. Construir a imagem Docker: `docker compose build`
3. Iniciar os containers: `docker compose up -d`
4. Remover os containers (Quando finalizar o uso): `docker compose down`

## Endpoints

### `POST /v1/project`
Cadastra um projeto.

#### Request Body
```json
{
  "name": "Projeto 1"
}
```

### `POST /v1/employee`
Cadastra um funcionário.

#### Request Body
```json
{
  "name": "Fulano",
  "email": "email@email.com",
  "cpf": "12345411212",
  "salary": 100.0
}
```

### `POST /v1/employee/add-project`
Associa um funcionário a um projeto.

#### Request Body
```json
{
  "employeeId": 1,
  "projectId": 1
}
```

### `GET /v1/project/with-employees`
Lista projetos com seus respectivos funcionários.

#### Request Body
N/A

#### Resposta
```json
[
  {
    "id": 1,
    "name": "Projeto 1",
    "dateOfCreation": "2024-04-15T16:15:35.555+00:00",
    "employees": [
      {
        "id": 1,
        "cpf": "12345411213",
        "email": "email@email.com",
        "name": "Fulano",
        "salary": 100.0
      }
    ]
  },
  {
    "id": 2,
    "name": "Projeto 2",
    "dateOfCreation": "2024-04-15T16:16:40.592+00:00",
    "employees": []
  },
  {
    "id": 3,
    "name": "Projeto 3",
    "dateOfCreation": "2024-04-15T19:20:02.619+00:00",
    "employees": [
      {
        "id": 2,
        "cpf": "12345411212",
        "email": "email@email1.com",
        "name": "Fulano",
        "salary": 100.0
      }
    ]
  }
]
```

### `GET /v1/project/{projectId}/employees`
Lista funcionários de um respectivo projeto.

#### Path Variables
`projectId` : ID do projeto. Deve ser um número.

#### Resposta
```json
[
  {
    "id": 2,
    "cpf": "12345411212",
    "email": "email@email1.com",
    "name": "Fulano 1",
    "salary": 100.0
  },
  {
    "id": 1,
    "cpf": "12345411213",
    "email": "email@email.com",
    "name": "Fulano 2",
    "salary": 100.0
  }
]
```

### `GET /v1/employee/{employeeId}/projects`
Lista projetos de um respectivo funcionário.

#### Path Variables
`employeeId` : ID do funcionário. Deve ser um número.

#### Resposta
```json
[
  {
    "id": 3,
    "name": "Projeto 3",
    "dateOfCreation": "2024-04-15T19:20:02.619+00:00"
  },
  {
    "id": 1,
    "name": "Projeto 1",
    "dateOfCreation": "2024-04-15T16:15:35.555+00:00"
  }
]
```

## Melhorias
- Implementar pipeline de CI/CD
- Adicionar documentação da API com Swagger
- Integrar com SonarQube
- Integrar com Trivy