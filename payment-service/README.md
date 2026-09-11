# payment-service

`payment-service` — микросервис платежей в Training Center Platform.

На текущем этапе сервис реализует базовые CRUD-операции для платежей. Более сложная бизнес-логика оплаты, Kafka-события и интеграция с `document-service` будут добавляться отдельными учебными задачами.

## Возможности

- создание платежа;
- получение платежа по ID;
- получение списка платежей;
- обновление платежа;
- удаление платежа;
- хранение данных в PostgreSQL;
- миграции через Liquibase;
- валидация входящих запросов;
- централизованная обработка ошибок;
- Swagger/OpenAPI;
- регистрация в Eureka;
- логирование через SLF4J + Logback.

## Стек

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- PostgreSQL 16
- Liquibase
- Bean Validation
- Swagger/OpenAPI
- Spring Cloud Netflix Eureka Client
- Lombok
- SLF4J + Logback

## Порт

По умолчанию сервис запускается на порту:

```text
8086
```

## База данных

По умолчанию используется:

```text
jdbc:postgresql://localhost:5438/payment_db
```

Параметры можно переопределить через переменные окружения:

```text
PAYMENT_DB_URL
PAYMENT_DB_USERNAME
PAYMENT_DB_PASSWORD
```

Структуру таблиц создаёт Liquibase.

## Статусы платежа

Используется enum `PaymentStatus`:

```text
PENDING
COMPLETED
FAILED
REFUNDED
```

При создании нового платежа начальный статус — `PENDING`.

## Endpoint'ы

### Создать платеж

```http
POST /api/payments
```

Пример запроса:

```json
{
  "enrollmentId": "11111111-1111-1111-1111-111111111111",
  "userId": "22222222-2222-2222-2222-222222222222",
  "amount": 15000.00,
  "currency": "RUB"
}
```

### Получить платеж по ID

```http
GET /api/payments/{id}
```

### Получить все платежи

```http
GET /api/payments
```

### Обновить платеж

```http
PUT /api/payments/{id}
```

### Удалить платеж

```http
DELETE /api/payments/{id}
```

## Swagger

После запуска Swagger UI доступен по адресу:

```text
http://localhost:8086/swagger-ui
```

OpenAPI JSON:

```text
http://localhost:8086/api-docs
```

## Eureka

Сервис регистрируется в `discovery-service` под именем:

```text
PAYMENT-SERVICE
```

Eureka Server по умолчанию:

```text
http://localhost:8761/eureka/
```

## Запуск

Перед запуском необходимо:

1. запустить PostgreSQL для `payment_db`;
2. запустить `discovery-service`;
3. запустить `payment-service`.

После запуска проверить:

- отсутствие ошибок Liquibase;
- подключение к PostgreSQL;
- регистрацию `PAYMENT-SERVICE` в Eureka;
- доступность Swagger UI.

## Структура

```text
payment-service
└── src/main/java/com/training/paymentservice
    ├── config
    ├── controller
    ├── dto
    ├── entity
    ├── exception
    ├── mapper
    ├── repository
    └── service
```

## Логирование

Для логирования используется стандартный стек Spring Boot:

```text
SLF4J + Logback
```

Основные операции создания, чтения, обновления и удаления платежей логируются на уровне `INFO`.

Пароли, токены и другие чувствительные данные логироваться не должны.

## Планируемое развитие

В следующих учебных задачах сервис можно расширить:

- имитацией обработки платежа;
- автоматической сменой `PENDING → COMPLETED/FAILED`;
- публикацией Kafka-событий `PaymentCompletedEvent` и `PaymentFailedEvent`;
- интеграцией с `document-service` для формирования фискального чека;
- отправкой уведомлений через `notification-service`.
