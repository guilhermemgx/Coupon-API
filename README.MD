# Coupon API

API REST em Java com Spring Boot para cadastro e exclusao logica de cupons.

O projeto foi desenvolvido para um desafio tecnico e concentra as regras principais no dominio, deixando controller e service mais focados em entrada, saida e persistencia.

## Arquitetura

O dominio foi separado da persistencia:

- `domain`: objetos de dominio puros, sem anotacoes JPA.
- `repository`: contrato usado pela aplicacao para salvar e consultar cupons.
- `repository.jpa`: implementacao com Spring Data JPA, entidade de banco e mapper.

As regras de negocio ficam em:

- `Coupon`
- `CouponCode`
- `DiscountValue`

A entidade JPA `CouponEntity` existe apenas para persistencia.

## Tecnologias

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Bean Validation
- H2 Database
- Swagger / OpenAPI
- Docker
- Docker Compose
- JUnit 5
- JaCoCo
- Maven

## Regras implementadas

### Criacao

- Campos obrigatorios: `code`, `description`, `discountValue` e `expirationDate`.
- O codigo informado pode conter caracteres especiais.
- Antes de salvar, o codigo e normalizado para 6 caracteres alfanumericos em maiusculo.
- O desconto minimo aceito e `0.5`.
- Nao e permitido criar cupom com data de expiracao no passado.
- O cupom pode ser criado como publicado usando o campo `published`.

### Exclusao

- A exclusao e logica.
- O registro permanece no banco com status `DELETED` e `deletedAt` preenchido.
- Nao e permitido deletar novamente um cupom ja deletado.

## Endpoints

| Metodo | Rota | Descricao |
| --- | --- | --- |
| `POST` | `/coupon` | Cria um cupom |
| `GET` | `/coupon` | Lista cupons ativos |
| `GET` | `/coupon/active` | Lista cupons ativos |
| `GET` | `/coupon/{id}` | Busca um cupom por id |
| `DELETE` | `/coupon/{id}` | Faz soft delete do cupom |

## Exemplos

Criar cupom:

```json
{
  "code": "abc-123",
  "description": "Cupom de teste",
  "discountValue": 0.8,
  "expirationDate": "2026-05-09T10:00:00Z",
  "published": true
}
```

Resposta:

```json
{
  "id": "f1b13aa2-4c2d-41e9-8b2f-1f9d37ce7e46",
  "code": "ABC123",
  "description": "Cupom de teste",
  "discountValue": 0.8,
  "expirationDate": "2026-05-09T10:00:00Z",
  "status": "ACTIVE",
  "published": true,
  "redeemed": false
}
```

## Como executar

Com Maven:

```bash
./mvnw spring-boot:run
```

Com Docker Compose:

```bash
docker compose up --build
```

Swagger:

```txt
http://localhost:8080/swagger-ui.html
```

H2 Console:

```txt
http://localhost:8080/h2-console
```

Configuracao do H2:

```txt
JDBC URL: jdbc:h2:mem:coupondb
User: sa
Password:
```

## Testes e cobertura

Rodar testes:

```bash
./mvnw test
```

Rodar verificacao com cobertura minima de 80%:

```bash
./mvnw verify
```

Relatorio JaCoCo:

```txt
target/site/jacoco/index.html
```
