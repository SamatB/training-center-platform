# notification-service

Микросервис уведомлений платформы **Training Center Platform**.

## Назначение

`notification-service` отвечает за создание и получение уведомлений, отправку Email и обработку событий, связанных с уведомлениями.

Сервис умеет:

- создавать и хранить уведомления в PostgreSQL;
- получать уведомление по UUID;
- отправлять Email через Gmail SMTP;
- получать `EnrollmentCreatedEvent` из Apache Kafka;
- получать данные пользователя из `user-service` через OpenFeign;
- отправлять пользователю Email после записи на курс;
- регистрироваться в Eureka.

## Технологии

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Liquibase
- Spring Validation
- Spring Mail
- Apache Kafka
- Spring Cloud OpenFeign
- Netflix Eureka Client
- Spring Cloud LoadBalancer
- Swagger / OpenAPI
- Lombok
- Maven

## Порт

```text
8084
```

## База данных

```text
notification_db
```

Подключение по умолчанию:

```text
jdbc:postgresql://localhost:5436/notification_db
```

Структура базы данных управляется через Liquibase.

## API

Базовый путь:

```text
/api/notifications
```

Основные endpoint'ы:

```text
POST /api/notifications
GET  /api/notifications/{id}
POST /api/notifications/email
```

## Swagger

После запуска сервиса Swagger UI доступен по адресу:

```text
http://localhost:8084/swagger-ui
```

OpenAPI JSON:

```text
http://localhost:8084/api-docs
```

## Email

Для отправки писем используется Gmail SMTP.

Необходимо задать переменные окружения:

```text
MAIL_USERNAME=example@gmail.com
MAIL_PASSWORD=пароль_приложения_Google
```

Обычный пароль от Google-аккаунта использовать не следует. Для SMTP используется пароль приложения Google.

## Kafka

Сервис подключается к Kafka:

```text
localhost:9092
```

Consumer Group:

```text
notification-service
```

Сервис слушает topic:

```text
enrollments.created
```

Текущая схема взаимодействия:

```text
enrollment-service
        ↓
EnrollmentCreatedEvent
        ↓
Apache Kafka
        ↓
notification-service
        ↓
OpenFeign → user-service
        ↓
Email пользователю
```

## Eureka

Сервис регистрируется в `discovery-service` под именем:

```text
notification-service
```

Eureka Server по умолчанию:

```text
http://localhost:8761/eureka/
```

## Логирование

Основные этапы обработки Kafka-событий логируются через SLF4J/Logback.

В логах можно увидеть получение `EnrollmentCreatedEvent` и успешную отправку уведомления пользователю.

Пароли, SMTP credentials и другие чувствительные данные логировать нельзя.

## Запуск

Перед запуском должны быть доступны:

- PostgreSQL;
- Kafka;
- `discovery-service`;
- `user-service` — для полного сценария обработки `EnrollmentCreatedEvent`.

Также должны быть заданы `MAIL_USERNAME` и `MAIL_PASSWORD`.

После запуска проверить:

1. сервис успешно подключился к `notification_db`;
2. Liquibase применил миграции;
3. сервис зарегистрирован в Eureka со статусом `UP`;
4. Kafka Consumer успешно подключился к Kafka;
5. Swagger UI открывается;
6. тестовое Email-сообщение успешно отправляется.
