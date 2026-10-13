# AGENTS.md

## Visão geral
Este projeto é um sistema de biblioteca em Kotlin com Quarkus, seguindo princípios de DDD, arquitetura hexagonal, CQRS e event-driven design.

Objetivo:
- Manter baixo acoplamento
- Priorizar regras de negócio no domínio
- Separar comandos, consultas, adaptadores e infraestrutura
- Manter módulos organizados por contexto de negócio
- Preservar o projeto em Quarkus e não voltar para Spring Boot

## Stack
- Kotlin
- Quarkus
- Hibernate ORM Panache
- MySQL
- Flyway
- Gradle
- DDD + Hexagonal + CQRS

## Estrutura do projeto
A estrutura base do projeto deve seguir esta organização:

src/main/kotlin/
application/
book/
commands/
domain/
events/
exceptions/
models/
services/
handlers/
ports/
user/
driven/
book/
user/
driver/
book/
user/
query/
book/
user/

## Convenções exigidas
- O domínio deve ser rico e não anêmico
- Entidades e aggregate roots devem conter comportamento
- Value Objects devem encapsular validações
- Domain events devem representar fatos do negócio
- Repositories devem ser interfaces no domínio e implementados em driven
- Handler/Use Case deve orquestrar casos de uso
- Controllers/REST devem ficar em driver
- Read models/projections devem ficar em query

## Regras de negócio
- ISBN deve ser validado como Value Object
- Livro inválido não deve ser persistido
- Usuário inativo não deve realizar empréstimos/reservas
- Empréstimo deve respeitar disponibilidade
- Reserva deve considerar concorrência e fila de espera
- Multa deve ser gerada somente após atraso
- Eventos de domínio devem ser rastreáveis e publicados quando o fato ocorrer

## Padrões esperados
### Domain
- `Book`, `User`, `Loan`, `Reservation`, `Fine` devem ter comportamento
- Não usar `data class` para Aggregate Root
- Use `value class` ou classes imutáveis para Value Objects
- Use exceptions de domínio para regras de negócio

### Application
- Comandos ficam em `application/.../commands`
- Handlers ficam em `application/.../handlers`
- Ports ficam em `application/.../ports`
- Casos de uso devem orquestrar regras e persistência

### Adaptadores
- `driver` = entrada do sistema (REST, HTTP, controllers, Kafka producers)
- `driven` = saída/infra (repositories, persistence, kafka consumers, DB)
- `query` = read model, query layer, projections e DTOs para leitura

## Nomenclatura
Use nomes consistentes, por exemplo:
- `CreateBookCommand`
- `CreateLoanCommand`
- `BookCreatedEvent`
- `LoanCreatedEvent`
- `BookRepository`
- `UserRepository`
- `CreateBookHandler`

## Validação
- Validações de negócio devem ficar no domínio
- Validações de entrada HTTP devem ficar em DTOs/Commands
- Regras críticas devem lançar exceções de domínio
- Evitar `if` espalhado sem encapsulamento

## Quarkus
- Manter o projeto em Quarkus
- Não reintroduzir Spring Boot
- Preferir CDI com `@ApplicationScoped`
- Usar propriedades Quarkus em `application.properties` ou `application.yml`
- Mantém estrutura modular por contextos

## Build e testes
Use os seguintes comandos normalmente:
```bash
./gradlew test
./gradlew build