# Course Service

`course-service` — микросервис Training Center Platform для управления курсами.

Сервис хранит основную информацию о курсах и предоставляет REST API для создания, получения, обновления и удаления курсов.

## Возможности

- создание курса;
- получение курса по ID;
- получение списка всех курсов;
- обновление курса;
- удаление курса;
- хранение данных в PostgreSQL;
- миграции через Liquibase;
- регистрация в Eureka Discovery Service;
- документация API через Swagger/OpenAPI;
- валидация входящих данных;
- централизованная обработка ошибок;
- логирование основных операций.

## Стек

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- PostgreSQL
- Liquibase
- Bean Validation
- Lombok
- Springdoc OpenAPI
- Spring Cloud Netflix Eureka Client

## Порт

По умолчанию сервис запускается на порту:

```text
8082
```

## База данных

Используется база:

```text
course_db
```

Локальное подключение:

```text
jdbc:postgresql://localhost:5434/course_db
```

Параметры подключения находятся в:

```text
src/main/resources/application.yml
```

## Основная модель Course

Курс содержит:

- `id` — уникальный UUID;
- `title` — название курса;
- `description` — описание;
- `teacherName` — имя преподавателя;
- `durationHours` — продолжительность в часах;
- `price` — стоимость;
- `active` — признак активности курса;
- `createdAt` — дата создания;
- `updatedAt` — дата обновления.

## REST API

Базовый путь:

```text
/api/v1/courses
```

### Создать курс

```http
POST /api/v1/courses
```

Пример запроса:

```json
{
  "title": "Java Backend",
  "description": "Курс по Java и Spring Boot",
  "teacherName": "Иван Иванов",
  "durationHours": 120,
  "price": 35000.00,
  "active": true
}
```

### Получить курс по ID

```http
GET /api/v1/courses/{id}
```

### Получить список курсов

```http
GET /api/v1/courses
```

### Обновить курс

```http
PUT /api/v1/courses/{id}
```

### Удалить курс

```http
DELETE /api/v1/courses/{id}
```

## Валидация

Проверяются:

- обязательное название курса;
- обязательное описание;
- обязательное имя преподавателя;
- продолжительность больше 0;
- цена не может быть отрицательной.

Сообщения Validation возвращаются на русском языке.

## Swagger / OpenAPI

После запуска Swagger UI доступен по адресу:

```text
http://localhost:8082/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8082/v3/api-docs
```

## Eureka Discovery Service

`course-service` регистрируется в Eureka под именем:

```text
course-service
```

Discovery Server:

```text
http://localhost:8761/eureka/
```

Eureka Dashboard:

```text
http://localhost:8761
```

## Liquibase

Основной changelog:

```text
src/main/resources/db/changelog/db.changelog-master.yaml
```

Liquibase создаёт таблицу:

```text
courses
```

## Логирование

Для логирования используется стандартный стек Spring Boot:

```text
SLF4J + Logback
```

Основные операции логируются на уровне `INFO`, отсутствие курса — на уровне `WARN`.

Пример последовательности:

```text
Получен запрос на получение курса
        ↓
Поиск курса в PostgreSQL
        ↓
Курс найден
        ↓
Ответ возвращён клиенту
```

## Запуск

Перед запуском должны работать:

1. PostgreSQL для `course_db`;
2. `discovery-service`.

После этого запустить:

```text
CourseServiceApplication
```

## Структура

```text
course-service
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── exception
├── mapper
├── repository
├── service
├── config
└── resources
    └── db/changelog
```

## Взаимодействие с другими сервисами

`course-service` является источником данных о курсах.

Другие сервисы могут получать информацию о курсе через REST/OpenFeign по имени сервиса в Eureka.

Например:

```text
enrollment-service
        ↓
OpenFeign
        ↓
course-service
```

В дальнейшем сервис может быть расширен Kafka-событиями и дополнительной логикой преподавателей и курсов.
