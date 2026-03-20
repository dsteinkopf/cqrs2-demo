# Architecture — CQRS + Event Sourcing Demo

## Data Flow

```mermaid
flowchart LR
    subgraph Demo
        Scripts["Shell Scripts<br/>(RabbitMQ HTTP API)"]
    end

    subgraph RabbitMQ
        CmdEx["offer.commands<br/>(Topic Exchange)"]
        EvtEx["offer.events<br/>(Topic Exchange)"]
    end

    subgraph Command Service
        Consumer["OfferCommandConsumer<br/>(@RabbitListener)"]
        Handler["OfferCommandHandler"]
        Aggregate["OfferAggregate<br/>(rehydrate → validate → event)"]
        ES["EventStore<br/>(JPA → PostgreSQL)"]
        Publisher["EventPublisher"]
    end

    subgraph Query Service
        EvtConsumer["OfferEventConsumer<br/>(@RabbitListener)"]
        Projector["OfferProjector"]
        ReadModel["offer_projection<br/>(PostgreSQL)"]
        API["REST API<br/>GET /api/offers"]
    end

    subgraph PostgreSQL
        EventTable[("event_store<br/>stream_id | sequence | event_type | payload")]
        ProjTable[("offer_projection<br/>offer_id | title | price | active")]
    end

    Client["Client<br/>(curl / browser)"]

    Scripts -->|"offer.create<br/>offer.update<br/>offer.deactivate"| CmdEx
    CmdEx --> Consumer
    Consumer --> Handler
    Handler --> Aggregate
    Aggregate --> ES
    ES --> EventTable
    Handler --> Publisher
    Publisher -->|"offer.*"| EvtEx
    EvtEx --> EvtConsumer
    EvtConsumer --> Projector
    Projector --> ReadModel
    ReadModel --> ProjTable
    API --> ReadModel
    Client -->|"GET"| API
```

## Two Exchanges — Clear Separation

| Exchange | Type | Purpose | Producer | Consumer |
|---|---|---|---|---|
| `offer.commands` | Topic | Inbound commands | Shell scripts | Command Service |
| `offer.events` | Topic | Domain events | Command Service | Query Service |

## Event Sourcing Flow

```mermaid
sequenceDiagram
    participant S as Shell Script
    participant R as RabbitMQ
    participant C as Command Service
    participant DB as PostgreSQL
    participant Q as Query Service

    S->>R: Publish CreateOfferCommand
    R->>C: Deliver to command-service.offer.create queue
    C->>DB: Load events for aggregate (empty)
    C->>C: OfferAggregate.create() → OfferCreatedEvent
    C->>DB: Append event to event_store
    C->>R: Publish OfferCreatedEvent to offer.events
    R->>Q: Deliver to query-service.offer.events queue
    Q->>DB: INSERT into offer_projection
