# Discovery Service

`discovery-service` — сервис обнаружения и регистрации микросервисов Training Center Platform.

Сервис работает как Eureka Server. Остальные микросервисы регистрируются в нём и могут находить друг друга по имени сервиса без жёсткой привязки к конкретному адресу и порту.

## Возможности

- запуск Eureka Server;
- регистрация микросервисов;
- отображение зарегистрированных экземпляров сервисов;
- Service Discovery для взаимодействия между микросервисами;
- Eureka Dashboard для просмотра состояния зарегистрированных сервисов.

## Технологии

- Java 21
- Spring Boot
- Spring Cloud Netflix Eureka Server
- Maven

## Порт

```text
8761
```

## Eureka Dashboard

После запуска сервиса панель Eureka доступна по адресу:

```text
http://localhost:8761
```

В разделе зарегистрированных экземпляров можно увидеть подключённые микросервисы и их состояние.

Например:

```text
USER-SERVICE
COURSE-SERVICE
ENROLLMENT-SERVICE
NOTIFICATION-SERVICE
AUTH-SERVICE
PAYMENT-SERVICE
DOCUMENT-SERVICE
```

Фактический список зависит от того, какие сервисы запущены и настроены как Eureka Client.

## Конфигурация

`discovery-service` является Eureka Server, поэтому сам не должен регистрироваться в Eureka и получать реестр у другого Eureka Server.

Основные настройки находятся в:

```text
src/main/resources/application.yml
```

## Запуск

Запустить:

```text
DiscoveryServiceApplication
```

После запуска открыть:

```text
http://localhost:8761
```

Затем можно запускать остальные микросервисы. При корректной настройке Eureka Client они появятся в Eureka Dashboard.

## Роль в архитектуре

Общая схема:

```text
                 discovery-service
                   Eureka Server
                        ↑
          ┌─────────────┼─────────────┐
          │             │             │
     user-service  course-service  enrollment-service
          │             │             │
          └─────────────┼─────────────┘
                        ↑
               остальные сервисы
```

Благодаря Service Discovery клиент может обращаться к другому сервису по его имени, например:

```text
user-service
course-service
notification-service
```

без жёсткого указания конкретного IP-адреса сервиса в бизнес-коде.

## Важно

Названия Java-классов, конфигурационных свойств, имён сервисов и других технических идентификаторов остаются на английском языке.

Документация и пользовательские пояснения проекта оформляются на русском языке.
