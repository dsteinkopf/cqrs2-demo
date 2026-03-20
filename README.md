# cqrs2 — Minimal CQRS + Event Sourcing Demo

A teaching demo for CQRS and Event Sourcing using Spring Boot, RabbitMQ, and PostgreSQL.

See [docs/architecture.md](docs/architecture.md) for the full architecture diagram.

## Prerequisites

- Java 21+
- Maven 3.9+
- Docker / Docker Compose

## Quick Start

### 1. Start Infrastructure

```bash
docker compose up -d
```

Starts PostgreSQL (port 5432) and RabbitMQ (ports 5672 + 15672 management UI).

### 2. Build

```bash
mvn clean compile
```

### 3. Start Services

In separate terminals:

```bash
# Terminal 1: Command Service (no web port — RabbitMQ consumer only)
cd command-service && mvn spring-boot:run

# Terminal 2: Query Service (port 8082)
cd query-service && mvn spring-boot:run
```

### 4. Demo

```bash
# Create an offer
./scripts/create-offer.sh "Aspirin" 9.99

# Query all offers (note the offerId in the response)
./scripts/query-offers.sh

# Update an offer
./scripts/update-offer.sh <offerId> "Aspirin 200mg" 12.99

# Query single offer
./scripts/query-offers.sh <offerId>

# Deactivate an offer
./scripts/deactivate-offer.sh <offerId>

# Verify deactivation
./scripts/query-offers.sh <offerId>
```

### 5. Inspect Event Store

```bash
docker compose exec postgres psql -U cqrs2 -c "SELECT * FROM event_store ORDER BY stream_id, sequence;"
```

## Architecture

- **Command Service**: Receives commands via RabbitMQ, validates via aggregate rehydration, persists events, publishes domain events
- **Query Service**: Consumes domain events, projects into read model, serves REST queries
- **No REST on write side** — all commands enter through RabbitMQ

## RabbitMQ Management UI

Open http://localhost:15672 (guest/guest) to inspect exchanges, queues, and messages.

## Cleanup

```bash
docker compose down -v
```
