# LOCAL-окружение Training Center Platform

Данная инструкция описывает процесс локальной разработки и отладки микросервисов в IntelliJ IDEA с использованием инфраструктуры (PostgreSQL, Kafka, Eureka), запущенной в Docker.

## Предварительные требования
- Java 21 (Eclipse Temurin)
- IntelliJ IDEA
- Docker Desktop
- Git

## Общая схема работы
1. Вся инфраструктура (PostgreSQL, Kafka) и остальные микросервисы запускаются в Docker.
2. Разрабатываемый микросервис запускается локально в IntelliJ IDEA.
3. Взаимодействие между IDEA и Docker осуществляется через `localhost` (для БД и Kafka) и `host.docker.internal` (для Eureka и OpenFeign).

## Инструкция по запуску сервиса локально

### 1. Создание application-local.yml
В папке src/main/resources/ каждого микросервиса создан файл application-local.yml.

Пример для notification-service:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5436/notification_db
    username: postgres
    password: postgres
  kafka:
    bootstrap-servers: localhost:9092
  cloud:
    openfeign:
      client:
        config:
          user-service:
            url: http://localhost:8081
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    hostname: host.docker.internal
    prefer-ip-address: false
```

### 2. Настройка Run Configuration в IntelliJ IDEA
1. Откройте Edit Configurations....
2. В поле Active profiles укажите: local.
3. Для сервисов, отправляющих email (например, notification-service), добавьте Environment variables:
    - MAIL_USERNAME
    - MAIL_PASSWORD
      (Значения берутся из файла infrastructure/.env).

### 3. Запуск инфраструктуры
Выполните в терминале (из корня проекта):
```bash
docker compose --env-file infrastructure/.env -f infrastructure/docker-compose.yml up -d
```

### 4. Остановка контейнера
Освободите порт для IDEA, остановив нужный контейнер:
```bash
docker stop <имя-сервиса>
```

### 5. Запуск в IDEA
Нажмите зеленую стрелочку (Run) в IntelliJ IDEA для запуска выбранного микросервиса.

### 6. Возврат в Docker
После завершения отладки верните контейнер обратно:
```bash
docker start <имя-сервиса>
```


## Решение частых проблем

**Ошибка Liquibase: relation "..." already exists**
База данных содержит таблицы от прошлых запусков. Очистите схему:
```bash
docker exec -it <имя-сервиса>-postgres psql -U postgres -d <имя_базы>
# внутри выполнить: DROP SCHEMA public CASCADE; CREATE SCHEMA public; \q
```

**Ошибка Connection refused к БД или Kafka**
Убедитесь, что контейнеры запущены (docker ps).

**IDEA не видит новый класс**
1. Удалите папку target в модуле.
2. Выполните Build -> Rebuild Project.
3. Или File -> Invalidate Caches / Restart.

**Gateway не видит локальный сервис (ошибка на фронтенде)**
Убедитесь, что в application-local.yml сервиса добавлен hostname:
```yaml
eureka:
  instance:
    hostname: host.docker.internal
    prefer-ip-address: false
```
## Дополнительно: запуск discovery-service (Eureka) локально
Смотрите пункт 5.9 техзадания. Для этого создан файл infrastructure/docker-compose.local-discovery.yml, и контейнеры перезапускаются с его использованием.