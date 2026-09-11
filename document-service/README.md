# document-service

`document-service` — микросервис Training Center Platform для хранения и управления метаданными документов.

На текущем этапе сервис содержит базовый CRUD. Генерация PDF, объектное хранилище и Kafka-интеграция будут добавляться отдельно.

## Стек

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Liquibase
- Jakarta Validation
- Lombok
- Springdoc OpenAPI / Swagger
- Eureka Client
- Maven

## Порт и база данных

По умолчанию сервис запускается на порту `8087`.

База данных:

```text
document_db
```

Подключение по умолчанию:

```text
jdbc:postgresql://localhost:5439/document_db
```

Параметры можно переопределить переменными окружения из `application.yml`.

## API

Базовый путь:

```text
/api/documents
```

Доступные операции:

```http
POST   /api/documents
GET    /api/documents
GET    /api/documents/{id}
PUT    /api/documents/{id}
DELETE /api/documents/{id}
```

## Типы документов

```text
RECEIPT
INVOICE
CERTIFICATE
```

## Статусы документов

```text
PENDING
GENERATED
FAILED
```

## Swagger

После запуска документация доступна по адресу:

```text
http://localhost:8087/swagger-ui/index.html
```

## Eureka

Сервис регистрируется в `discovery-service` под именем:

```text
document-service
```

## Liquibase

Структура БД создаётся и изменяется через Liquibase.

Главный changelog:

```text
src/main/resources/db/changelog/db.changelog-master.yaml
```

## Логирование

Основные операции Controller и Service логируются через `SLF4J + Logback`.

В логах не должны выводиться пароли, токены и другие чувствительные данные.

## Запуск

1. Запустить PostgreSQL для `document_db`.
2. Запустить `discovery-service`, если требуется регистрация в Eureka.
3. Запустить `DocumentServiceApplication`.
4. Проверить Swagger и подключение к БД.

## Дальнейшее развитие

В следующих задачах сервис можно расширить:

- генерацией PDF-документов;
- созданием фискальных чеков после успешной оплаты;
- интеграцией с S3-совместимым хранилищем, например MinIO;
- Kafka Producer/Consumer;
- публикацией события после успешной генерации документа.
