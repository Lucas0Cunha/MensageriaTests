# Microsserviços de pedidos

Projeto de estudo com uma API de pedidos e dois consumidores assíncronos usando Spring Boot, RabbitMQ, PostgreSQL e MailHog.

## Arquitetura

- `pedidos-api`: expõe a API REST e publica pedidos no RabbitMQ.
- `processador`: consome os pedidos, altera o status para `PROCESSADO` e salva no PostgreSQL.
- `notificacao`: consome os pedidos e envia um e-mail de notificação pelo MailHog.
- `docker-compose.yml`: inicia RabbitMQ, MailHog e PostgreSQL.

O projeto utiliza Java 21 e Spring Boot 4.1.1.

## Pré-requisitos

- Java 21
- Docker e Docker Compose
- Maven Wrapper disponível em cada módulo

## Subir as dependências

Na raiz do projeto, execute:

```bash
docker compose up -d
```

Serviços disponíveis:

| Serviço | Endereço |
| --- | --- |
| API de pedidos | `http://localhost:8080` |
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| RabbitMQ Management | `http://localhost:15672` |
| MailHog | `http://localhost:8025` |
| PostgreSQL | `localhost:5433` |

Credenciais locais do RabbitMQ:

- Usuário: `user`
- Senha: `password`

Credenciais locais do PostgreSQL:

- Banco: `pedidos_db`
- Usuário: `user`
- Senha: `password`

## Executar os serviços

Abra um terminal para cada módulo e execute:

```bash
cd pedidos-api
./mvnw spring-boot:run
```

```bash
cd processador
./mvnw spring-boot:run
```

```bash
cd notificacao
./mvnw spring-boot:run
```

No Windows PowerShell, use `mvnw.cmd` no lugar de `./mvnw`.

## Criar um pedido

Endpoint:

```http
POST http://localhost:8080/api/v1/pedidos
Content-Type: application/json
```

Exemplo:

```json
{
  "cliente": "Lucas Cunha",
  "itens": [],
  "valorTotal": 150.0,
  "emailNotificacao": "cliente@example.com"
}
```

Exemplo com cURL:

```bash
curl -X POST http://localhost:8080/api/v1/pedidos \
  -H "Content-Type: application/json" \
  -d '{"cliente":"Lucas Cunha","itens":[],"valorTotal":150.0,"emailNotificacao":"cliente@example.com"}'
```

## Fluxo de mensagens

1. `pedidos-api` publica o pedido na exchange fanout `pedidos.v1.pedidod-criado`.
2. O RabbitMQ distribui a mensagem para as filas dos consumidores.
3. `processador` consome a fila `pedidos.v1.pedidos-criado.gerar-processamento` e grava o pedido no banco.
4. `notificacao` consome a fila `pedidos.v1.pedidos-criado.gerar-notificacao` e envia o e-mail.
5. O e-mail enviado pelo MailHog pode ser visualizado em `http://localhost:8025`.

## Dead Letter Exchange e Dead Letter Queue

A fila de notificações possui a propriedade `x-dead-letter-exchange` apontando para:

```text
pedidos.v1.pedidos-criado.dlx
```

Essa exchange está ligada à fila:

```text
pedidos.v1.pedidos-criado.gerar-notificacao.dlq
```

Quando o processamento falha, o Spring tenta novamente até quatro vezes. Depois disso, como o requeue está desativado, o RabbitMQ rejeita a mensagem e a encaminha automaticamente para a DLX e, em seguida, para a DLQ.

Também existe uma regra de teste no serviço de notificações: pedidos com `valorTotal` maior que `2000` geram uma exceção e podem ser observados na DLQ após as tentativas.

## Testes e empacotamento

Em qualquer módulo:

```bash
./mvnw test
./mvnw clean package
```

No Windows PowerShell:

```powershell
.\mvnw.cmd test
.\mvnw.cmd clean package
```

## Observação sobre nomes

O nome atual da exchange principal é `pedidos.v1.pedidod-criado`, conforme configurado nos três serviços. O trecho `pedidod` parece ser um erro de digitação, mas deve permanecer igual em todos os serviços até que seja alterado de forma coordenada.
