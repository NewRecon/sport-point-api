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
| **`subscriptions_history`** | `id` (PK), `user_id`, `event_id`, `subscribed_at`, `participant_role` | История активности пользователя (для отображения в профиле) |

### `events_db` (Event Service + Subscription Service)
| Таблица | Поля | Описание |
| :--- | :--- | :--- |
| **`events`** | `id` (PK), `title`, `description`, `date`, `location_lat`, `location_lng`, `organizer_id`, `total_participants`, `status` | Метаданные ивентов с координатами для карты |
| **`subscriptions`** | `id` (PK), `user_id`, `event_id`, `subscribed_at` | Факты подписок на ивенты |

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
    subgraph KafkaBus["Apache Kafka — асинхронная шина событий"]
        direction LR
        T1[event-events]
        T2[sub-events]
        T3[profile-events]
    end
    
    %% Публикации в шину
    Event -. "publish" .-> T1
    Sub -. "publish" .-> T2
    Profile -. "publish" .-> T3
    
    %% Подписки потребителей
    T2 -. "consume" .-> Profile
    T3 -. "consume" .-> Sub
    T1 -. "consume" .-> Profile
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
REST-запрос обрабатывается синхронно. Счётчик мест в Redis инициализируется через Subscription Service. В профиле создателя отображается новая подпистка с ролью "OWNER".

```mermaid
sequenceDiagram
    participant C as Frontend
    participant GW as API Gateway
    participant ES as Event Service
    participant DB as PostgreSQL (events_db)
    participant K as Kafka
    participant PS as Profile Service
    participant SS as Subscription Service
    participant R as Redis

    C->>GW: POST /events (JWT)
    GW->>ES: Forward Request
    ES->>DB: INSERT INTO events
    ES-)K: Publish: EventCreatedEvent (async)
    ES-->>GW: 201 Created
    GW-->>C: 201 Created + event data
    K-)PS: Consume Event
    K-)SS: Consume Event
    SS->>R: INCR (создание счётчика мест)
```

#### Сценарий 2: Подписка пользователя на ивент
Ключевой сценарий — именно здесь Redis показывает всю свою мощь. Атомарный DECR защищает от овербукинга даже при тысячах одновременных запросов.

```mermaid
sequenceDiagram
    participant C as Frontend
    participant GW as API Gateway
    participant SS as Subscription Service
    participant R as Redis
    participant DB as PostgreSQL (events_db)
    participant K as Kafka
    participant PS as Profile Service

    C->>GW: POST /subscribe (JWT)
    GW->>SS: Forward Request
    SS->>R: DECR event:{eventId}:subs
    alt Мест нет (результат < 0)
        SS->>R: INCR (откат)
        SS-->>GW: 400 Bad Request
        GW-->>C: 400 "No subs available"
    else Место есть
        SS->>DB: INSERT INTO subs (CONFIRMED)
        SS-)K: Publish: UserSubscribedEvent {userId, eventId}
        SS-->>GW: 200 OK
        GW-->>C: 200 OK
        K-)PS: Consume Event
    end
```

#### Сценарий 3: Удаление пользователя (каскадная очистка)
Пользователь удаляет аккаунт → Profile Service чистит свои таблицы → через Kafka Event Service возвращает места в продажу.

```mermaid
sequenceDiagram
    participant C as Frontend
    participant GW as API Gateway
    participant PS as Profile Service
    participant DB1 as profile_db
    participant K as Kafka
    participant SS as Subscription Service
    participant R as Redis
    participant DB2 as events_db

    C->>GW: DELETE /profiles/{id} (JWT)
    GW->>PS: Forward Request
    PS->>DB1: Soft delete user + history
    PS-)K: Publish: UserDeletedEvent {userId}
    PS-->>GW: 200 OK
    GW-->>C: 200 OK
    K-)SS: Consume Event
    SS->>DB2: SELECT subs WHERE user_id = ?
    loop Для каждого активного билета
        SS->>R: INCR event:{eventId}:subs
        SS->>DB2: UPDATE sub SET status = CANCELLED
    end
```

#### Сценарий 4: Удаление ивента
Организатор отменяет мероприятие → Event Service чистит свои данные → Profile Service асинхронно удаляет подписки из профилей пользователей.
```mermaid
sequenceDiagram
    participant C as Frontend
    participant GW as API Gateway
    participant ES as Event Service
    participant DB1 as events_db
    participant K as Kafka
    participant SS as Subscription Service
    participant R as Redis
    participant PS as Profile Service
    participant DB2 as profile_db

    C->>GW: DELETE /events/{id} (JWT)
    GW->>ES: Forward Request
    ES->>DB1: Soft delete events
    ES->>DB1: Soft delete sub
    ES-)K: Publish: EventDeletedEvent {eventId}
    ES-->>GW: 200 OK
    GW-->>C: 200 OK
    K-)SS: Consume Event
    SS->>R: delete event cache
    K-)PS: Consume Event
    PS->>DB2: Soft delete subscriptions_history
```