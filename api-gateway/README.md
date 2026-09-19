# API Gateway

## Назначение
Что такое Gateway и какую роль выполняет в Training Center Platform.

## Порт
8080

## Технологии
Java 21
Spring Boot
Spring Cloud Gateway
Spring WebFlux
Eureka Client
Actuator

## Архитектура
Client → API Gateway → Eureka → Microservice

## Маршрутизация
Как работают Route, Predicate, Path и lb://.

## Текущие маршруты
Пока course-service как реализованный пример.

## Eureka
Как Gateway регистрируется и как находит сервисы.

## Global Filters
LoggingFilter:
- REQUEST
- RESPONSE
- HTTP status
- время выполнения
- IP клиента

## CORS
Почему CORS настроен централизованно в Gateway.
Разрешён frontend http://localhost:5173.

## Запуск
Что необходимо запустить перед Gateway и как проверить.

## Проверка
Пример прямого запроса:
localhost:8082/api/v1/courses/{id}

Пример через Gateway:
localhost:8080/api/v1/courses/{id}

## Что будет добавлено дальше
- маршрутизация остальных сервисов
- Correlation ID
- обработка ошибок
- JWT Security
- RSA/Public Key
- JWKS