# Sport Point

## Тема проекта
Разработка высоконагруженной **Event-driven микросервисной платформы** для создания, поиска и участия в городских спортивных ивентах с интерактивной картой, системой профилей и асинхронным взаимодействием между сервисами.

## Суть проекта
**Sport Point** — это платформа, которая объединяет активных людей города. Основные возможности:

- **Карта ивентов (афиша)** — пользователи видят все мероприятия на интерактивной карте города
- **Создание ивентов** — любой пользователь может организовать своё событие (пробежка, турнир, тренировка и т.д.)
- **Подписка и участие** — можно записаться на интересующее ивент и увидеть список других участников
- **Профиль пользователя** — каждый может рассказать о себе, своих интересах и спортивной активности
- **История активности** — в профиле отображаются все ивенты, в которых пользователь участвовал
---

## Стек технологий

| Категория | Технологии |
| :--- | :--- |
| **Языки и фреймворки** | Java, Spring Boot, Spring WebFlux |
| **Работа с данными** | Spring Data JPA, Spring Data Redis, Spring Kafka |
| **Базы данных** | PostgreSQL|
| **Кэширование** | Redis |
| **Брокер сообщений** | Apache Kafka|
| **Инфраструктура** | Docker, Docker Compose |
| **Безопасность** | Spring Security, JWT|
| **Логирование** | Slf4j|
| **Архитектурные паттерны** | API Gateway, Event-Driven Architecture, Saga (хореография) |

---

## Структура проекта (Микросервисы и БД)

Проект построен по принципу **Database-per-Service / Domain**. Каждый сервис владеет своей предметной областью и своими данными.

```mermaid
graph TB
    subgraph "Sport Point Platform"
        GW[API Gateway]
        
        subgraph "Domain Services"
            AUTH[Auth Service]
            PROF[Profile Service]
            EVT[Event Service]
            SUB[Subscription Service]
        end
        
        subgraph "Data Layer"
            DB1[(auth_db<br/>PostgreSQL)]
            DB2[(profile_db<br/>PostgreSQL)]
            DB3[(events_db<br/>PostgreSQL)]
            REDIS[(Redis<br/>Счётчики мест)]
        end
    end
    
    GW --> AUTH
    GW --> PROF
    GW --> EVT
    GW --> SUB
    
    AUTH --> DB1
    PROF --> DB2
    EVT --> DB3
    SUB --> DB3
    SUB --> REDIS
```
### Детальная таблица сервисов

| Микросервис | Ответственность | База данных | Ключевые эндпоинты |
| :--- | :--- | :--- | :--- |
| **Gateway** | Маршрутизация, балансировка, JWT-валидация, обогащение заголовков | — | Проксирует все запросы |
| **Auth Service** | Регистрация, аутентификация, выпуск JWT, CRUD пользователей | `auth_db` (PostgreSQL) | `/register`, `/login`, `/users/**` |
| **Profile Service** | Публичные профили, история активности, подписки пользователя | `profile_db` (PostgreSQL) | `/profiles/**` |
| **Event Service** | Создание и управление ивентами, метаданные мероприятий | `events_db` (PostgreSQL) | `/events/**` |
| **Subscription Service** | Подписки на ивенты, владение "местами", билетами | `events_db` (PostgreSQL) + **Redis** | `/subscriptions/**` |

---

## Схема базы данных

### `auth_db` (Auth Service)
| Таблица | Поля | Описание |
| :--- | :--- | :--- |
| **`users`** | `id` (PK), `username`, `password_hash`, `role`, `created_at`, `status` | Учётные записи пользователей |

###  `profile_db` (Profile Service)
| Таблица | Поля | Описание |
| :--- | :--- | :--- |
| **`profiles`** | `id` (PK), `user_id` (FK), `bio`, `avatar_url`, `location`, `status` | Публичные профили с информацией о себе |

### `events_db` (Event Service + Subscription Service)
| Таблица | Поля | Описание |
| :--- | :--- | :--- |
| **`events`** | `id` (PK), `title`, `description`, `date`, `location_lat`, `location_lng`, `organizer_id`, `total_participants`, `status` | Метаданные ивентов с координатами для карты |
| **`subscriptions`** | `id` (PK), `user_id`, `event_id`, `participant_role`, `subscribed_at` | Факты подписок на ивенты |

### Redis (только Subscription Service)
| Ключ | Тип | Описание |
| :--- | :--- | :--- |
| `event:{eventId}:subs` | String (Integer) | Атомарный счётчик свободных мест. Операции `DECR` / `INCR` |
---

## Общая схема взаимодействия
```mermaid
graph TD
    %% Синхронный путь (сверху вниз)
    Client[Frontend / Mobile App] --> GW[API Gateway<br/>WebFlux + JWT]
    
    subgraph Services["Микросервисы"]
        Auth[Auth Service]
        Profile[Profile Service]
        Event[Event Service]
        Sub[Subscription Service]
    end
    
    subgraph Data["Хранилища данных"]
        DB1[(auth_db<br/>PostgreSQL)]
        DB2[(profile_db<br/>PostgreSQL)]
        DB3[(events_db<br/>PostgreSQL)]
        Redis[(Redis<br/>Счётчики мест)]
    end
    
    %% Синхронные связи: Gateway → Services
    GW --> Auth
    GW --> Profile
    GW --> Event
    GW --> Sub
    
    %% Синхронные связи: Services → Data
    Auth --> DB1
    Profile --> DB2
    Event --> DB3
    Sub --> DB3
    Sub --> Redis
    
    %% Асинхронный путь (через Kafka-шину)
    subgraph KafkaBus["Kafka"]
        direction LR
        T1[event-events]
    end
    
    %% Публикации в шину
    Event -. "publish" .-> T1
    
    %% Подписки потребителей
    T1 -. "consume" .-> Sub
    
    %% Стили
    classDef sync fill:#e3f2fd,stroke:#1976d2,stroke-width:2px,color:#0d47a1
    classDef async fill:#fff3e0,stroke:#f57c00,stroke-width:2px,color:#e65100
    classDef data fill:#f3e5f5,stroke:#7b1fa2,stroke-width:2px,color:#4a148c
    
    class GW,Auth,Profile,Event,Sub sync
    class T1,T2,T3 async
    class DB1,DB2,DB3,Redis data
```

### Архитектурный принцип
- Синхронный вход — клиент всегда получает мгновенный ответ от сервиса, которому адресован запрос
- Асинхронный "хвост" — каскадные операции (очистка подписок, возврат мест) выполняются через Kafka
- Атомарность критичных операций — Redis гарантирует корректную работу с местами даже под нагрузкой
- Слабая связанность — сервисы не знают друг о друге, общаются только через события
- Отказоустойчивость — падение одного сервиса не валит всю систему
- Масштабируемость — каждый сервис можно масштабировать независимо
- Быстрый отклик — клиент получает ответ мгновенно, "грязная работа" уходит в фон
- Отсутствие блокировок и "эффекта домино" при падении одного из сервисов

### Бизнес-сценарии

#### Сценарий 1: Создание ивента на карте
Пользователь создаёт ивент → через Kafka Subscription Service создаёт подписку с ролью "OWNER", Счётчик мест в Redis инициализируется

```mermaid
sequenceDiagram
    participant C as Frontend
    participant GW as API Gateway
    participant ES as Event Service
    participant DB as events_db
    participant K as Kafka
    participant SS as Subscription Service
    participant DB2 as subscription_db
    participant R as Redis

    C->>GW: POST /events (JWT)
    GW->>ES: Forward Request
    ES->>DB: INSERT INTO events
    ES-)K: Publish: EventCreatedEvent (async)
    ES-->>GW: 201 Created
    GW-->>C: 201 Created + event data
    K-)SS: Consume Event
    SS->>DB2: INSERT INTO subscriptions
    SS->>R: INCR (создание счётчика мест)
```

#### Сценарий 2: Подписка пользователя на ивент
Пользователь создаёт подписку, Redis проверяет наличие мест DECR

```mermaid
sequenceDiagram
    participant C as Frontend
    participant GW as API Gateway
    participant SS as Subscription Service
    participant R as Redis
    participant DB as subscription_db

    C->>GW: POST /subscribe (JWT)
    GW->>SS: Forward Request
    SS->>R: DECR event:{eventId}:subs
    alt Мест нет (результат < 0)
        SS->>R: INCR (откат)
        SS-->>GW: 400 Bad Request
        GW-->>C: 400 "No subs available"
    else Место есть
        SS->>DB: INSERT INTO event
        SS-->>GW: 200 OK
        GW-->>C: 200 OK
    end
```

#### Сценарий 3: Удаление подписки
Пользователь отменяет подписку → Redis INCR

```mermaid
sequenceDiagram
    participant C as Frontend
    participant GW as API Gateway
    participant SS as Subscription Service
    participant DB as subscription_db
    participant R as Redis

    C->>GW: DELETE /events/{id} (JWT)
    GW->>SS: Forward Request
    SS->>DB: Soft delete subscriptions
    SS->>R: INCR event:{eventId}:subs
    SS-->>GW: 200 OK
    GW-->>C: 200 OK
```

#### Сценарий 4: Удаление ивента
Пользователь удаляет ивент → через Kafka Subscription Service отменяет подписки

```mermaid
sequenceDiagram
    participant C as Frontend
    participant GW as API Gateway
    participant ES as Event Service
    participant DB1 as events_db
    participant K as Kafka
    participant SS as Subscription Service
    participant DB2 as subscription_db
    participant R as Redis

    C->>GW: DELETE /profiles/{id} (JWT)
    GW->>ES: Forward Request
    ES->>DB1: Soft delete events
    ES-)K: Publish: EventDeletedEvent
    ES-->>GW: 200 OK
    GW-->>C: 200 OK
    K-)SS: Consume Event
    SS->>DB2: Soft delete subscriptions
    loop Для каждого активного билета
        SS->>R: INCR event:{eventId}:subs
    end
```