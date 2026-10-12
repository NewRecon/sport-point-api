# Sport Point API

Backend platform for organizing urban sports events, built on the principles of **Event-Driven Architecture** with a microservices approach.

## About the Project

**Sport Point** is a platform that brings together active people in the city. Users can find sports events on an interactive map, create their own events, sign up to participate, and maintain a profile of their sports activity.

This repository contains **only the API part** of the project (backend).

### Core Capabilities

- **Event map** — view all events on an interactive city map with filtering
- **Event creation** — any user can organize an event (training session, competition, etc.)
- **Subscription and participation** — sign up for an event and view the list of participants
- **User profile** — information about oneself, interests, and sports activity

## Technology Stack

| Category | Technologies |
|---|---|
| Languages and frameworks | Java, Spring Boot, Spring WebFlux |
| Data access | Hibernate, Spring Data JPA, Spring Data Redis, Spring Kafka |
| Databases and cache | PostgreSQL, Redis, S3 (rustFS) |
| Message broker | Apache Kafka |
| Infrastructure | Docker, Nginx |
| Security | Spring Security, JWT |
| Build systems | Maven |

## Services

The backend is split into several microservices, each owning its own data and communicating with the others exclusively through events.

**Gateway Service.** The single entry point for all client requests. It validates the incoming JWT, parses its claims, and injects custom headers (such as user ID, roles, and other identity data) into the requests forwarded to downstream services. This means internal services never deal with token parsing themselves — they simply read trusted headers placed by the gateway, keeping authentication concerns centralized and the downstream services free of security boilerplate.

**Auth Service.** Handles registration and login, issues JWT tokens, and publishes the `UserRegistered` event that triggers cascading operations in the rest of the system. This is the service the client talks to synchronously during registration, before the asynchronous "tail" kicks in.

**Profile Service.** Owns user profile data (display name, bio, interests, sports activity). It consumes user-related events and maintains its own read models, so profile data can be served without querying another services.

**Event Service.** Manages the lifecycle of sports events: creation, editing, and retrieval. It owns the event data and publishes event-related domain events to Kafka for other services to consume.

**Subscription Service.** Handles sign-ups for events, which is the most contended area of the system. It tracks available slots with an atomic counter in Redis, so that dozens or hundreds of simultaneous sign-ups for the same event do not cause row locks or waiting queues. It scales independently and its failure does not affect event creation and reads.

## Architecture

The platform is built on the principles of **Event-Driven Architecture**, where everything revolves around events. This manifests across four dimensions: interaction, data, reads, and operations.

**Interaction.** Services communicate exclusively through events, with no REST calls between them. Each service publishes events and subscribes to others' events without knowing of each other's existence. The client receives an immediate response from the service the request is addressed to, while cascading operations (profile creation, subscriptions) are performed in the background via Apache Kafka — a pattern of synchronous entry with an asynchronous "tail". This yields loose coupling: services do not depend on the availability of their neighbors at the moment of request processing, so the failure of one service does not block the others.

**Data.** Each service owns its data following the Database-per-Service principle: databases are not shared, and accessing another service's data is only possible through events. State is transferred via events using the Event-Carried State Transfer (ECST) pattern — an event carries not only the fact that something happened but also the state itself, allowing the recipient to update its data without a callback request to the source. Only immutable fields are denormalized; mutable data is not duplicated, avoiding desynchronization between services.

**Reads and operations.** Writes and reads are separated following CQRS: separate read models (projections) are built from events and updated asynchronously. Distributed business operations follow the Saga pattern in its choreography form — a chain of events without a central orchestrator, where each service reacts to events and publishes its own.

## Fault Tolerance

Fault tolerance is achieved through a combination of architectural patterns that together provide guaranteed, exactly-once-effective message processing on top of Kafka's at-least-once delivery.

**Transactional Outbox** solves the problem of atomically writing a change to the database and publishing an event to Kafka. Since performing the two actions sequentially risks a crash in between (the database write succeeds but the Kafka publish does not, or vice versa), the event is written to a separate `outbox` table within the same transaction as the domain change. A separate scheduler then reads the outbox and publishes to Kafka, marking the record as sent. This guarantees, on the producer side, that the event will be sent.

**Consumer Manual Commit** solves the problem of a consumer acknowledging a message before actually processing it. With auto-commit enabled, a crash after the commit but before processing means the message is lost. Auto-commit is therefore disabled, and the offset is committed manually only after successful processing (ideally in the same transaction as the database write). The principle becomes "processed — acknowledged" rather than the other way around, forming the foundation for at-least-once delivery combined with idempotency.

**Idempotency (Inbox Pattern)** addresses the consequence of at-least-once delivery: a message may arrive repeatedly on rebalance or retry, and without protection, repeated processing corrupts state (double DECR, duplicate INSERT, repeated events). On the consumer side, an Inbox table is used symmetrically to Outbox on the producer side: each event carries an `idempotency-key` header, the consumer attempts to insert the key into `inbox_message` via `saveAndFlush`, and on a duplicate the message is acknowledged and processing is skipped, while a missing header raises `IllegalArgumentException` and leads straight to the DLT. This guarantees, on the consumer side, that a repeatedly arriving event is not processed twice.

**Retry Topic** handles transient errors such as an unavailable receiving service, a database under load, or a timeout on an external system call — cases where retrying the message makes sense. Retrying in the main topic is not acceptable because it blocks the partition and slows down processing of all other messages. Instead, on error the message is moved to a separate retry topic with delayed retries and a limited number of attempts, with retry topics numbered by attempt index to make system behavior observable. Transient failures are thus survived without message loss and without blocking the main stream, and the load on the failing service grows gradually rather than like an avalanche.

**DLT (Dead Letter Topic)** handles errors that are non-retryable by nature: a malformed message, a missing required field or header, or a logical error in the data. Retrying such messages is pointless and harmful, as they clog the topics and mask the real problem. Errors are therefore explicitly classified as retryable and non-retryable: non-retryable exceptions skip the retry cycle and go straight to the DLT, and everything that has exhausted its attempt limit in the retry topic ends up there as well. Messages in the DLT are not lost — they can be examined, fixed, and replayed — so "dead" messages neither block the system nor disappear without a trace, and the DLT contents serve as the point of diagnostics and manual intervention.

## Running the Project

The project is launched via a **dev container**.

> The dev container configuration provides a reproducible development environment with all required dependencies. Detailed setup instructions will be added.
