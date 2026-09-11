# Enrollment Service

`enrollment-service` — микросервис Training Center Platform, отвечающий за записи студентов на курсы.

## Возможности

- создание записи студента на курс;
- получение записи по идентификатору;
- получение списка всех записей;
- обновление записи;
- удаление записи;
- хранение данных в PostgreSQL;
- миграции базы данных через Liquibase;
- регистрация сервиса в Eureka;
- публикация `EnrollmentCreatedEvent` в Apache Kafka после создания записи;
- Swagger/OpenAPI для просмотра и проверки REST API.

## Технологии

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Liquibase
- Spring Validation
- Spring Cloud OpenFeign
- Netflix Eureka Client
- Apache Kafka
- Springdoc OpenAPI

## Порт

```text
8083
```

## База данных

```text
enrollment_db
```

По умолчанию PostgreSQL доступен на:

```text
localhost:5435
```

## Kafka

Сервис публикует событие после успешного создания записи на курс.

Топик:

```text
enrollments.created
```

Событие:

```text
EnrollmentCreatedEvent
```

Kafka по умолчанию:

```text
localhost:9092
```

## Eureka

Сервис регистрируется в `discovery-service`.

```text
http://localhost:8761/eureka/
```

## REST API

Базовый путь:

```text
/api/v1/enrollments
```

Основные endpoint'ы:

```text
POST   /api/v1/enrollments
GET    /api/v1/enrollments/{id}
GET    /api/v1/enrollments
PUT    /api/v1/enrollments/{id}
DELETE /api/v1/enrollments/{id}
```

## Swagger

После запуска сервиса Swagger UI доступен по адресу:

```text
http://localhost:8083/swagger-ui/index.html
```

## Запуск

Перед запуском необходимо убедиться, что доступны:

- PostgreSQL;
- Kafka;
- `discovery-service`.

После запуска проверить отсутствие ошибок подключения к PostgreSQL, Kafka и Eureka.

## Логирование

Основные действия сервиса логируются на русском языке. При публикации Kafka-события в логах отображаются топик, partition и offset.

## Важно

Названия Java-классов, методов, endpoint'ов, полей DTO, Kafka-событий и других технических идентификаторов используются на английском языке. Пользовательская документация, Swagger-описания, сообщения валидации и прикладные логи оформляются на русском языке.
