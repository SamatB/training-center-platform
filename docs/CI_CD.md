# CI/CD в Training Center Platform

## Что такое CI/CD?

**CI (Continuous Integration)** — автоматическая проверка и сборка проекта после изменения кода.

**CD (Continuous Delivery / Deployment)** — доставка готового приложения в окружение, где оно будет запущено.

В нашем проекте для CI используется **GitHub Actions**, для хранения Docker-образов — **GHCR**, а для запуска всей системы — **Docker Compose**.

---

## Как мы работаем с кодом?

Новую задачу выполняем в отдельной ветке:

```text
main
 ↓
feature/...
 ↓
commit
 ↓
push
 ↓
Pull Request → main
```

После создания Pull Request GitHub автоматически запускает CI.

---

## Что делает CI?

GitHub Actions запускается на временной Linux-машине — **GitHub Runner**.

Для backend выполняется:

```text
Compile
 ↓
Tests + Testcontainers
 ↓
JaCoCo
 ↓
SonarQube
 ↓
Package
```

Кратко:

- **Compile** — проверяет, что Java-код компилируется.
- **Tests** — запускает автоматические тесты.
- **Testcontainers** — позволяет интеграционным тестам запускать реальные PostgreSQL/Kafka в Docker.
- **JaCoCo** — собирает покрытие кода тестами.
- **SonarQube Cloud** — анализирует качество кода.
- **Package** — создаёт `.jar` файлы.

Если какой-либо этап падает, CI становится красным. Нужно открыть упавший job в GitHub Actions и посмотреть лог.

---

## Что происходит после Merge?

После успешного CI Pull Request можно объединить с `main`.

Изменение `main` снова запускает GitHub Actions.

Теперь кроме проверок создаются Docker-образы:

```text
Backend
Java → Maven → JAR → Docker image

Frontend
React → npm build → Nginx → Docker image
```

Готовые образы отправляются в **GitHub Container Registry (GHCR)**:

```text
ghcr.io/samatb/user-service:ci
ghcr.io/samatb/auth-service:ci
ghcr.io/samatb/api-gateway:ci
ghcr.io/samatb/frontend:ci
...
```

Для backend-сервисов используется **matrix strategy** — GitHub Actions параллельно собирает отдельный image для каждого сервиса.

---

## Как запускается приложение?

Docker Compose получает готовые образы из GHCR:

```text
GHCR
 ↓
docker compose pull
 ↓
docker compose up -d
 ↓
Training Center Platform
```

В Docker запускаются:

```text
Frontend (Nginx)
        ↓
API Gateway
        ↓
Eureka
        ↓
Microservices
        ↓
PostgreSQL / Kafka
```

Frontend обращается только к:

```text
http://localhost:8080
```

Это **API Gateway**. Frontend не должен знать адреса отдельных микросервисов.

---

## Где хранятся секреты?

Секретные данные нельзя хранить в Git.

Сейчас в нашем проекте в файле `.env` хранятся секретные данные **Google для отправки email из `notification-service`**.

Файл находится здесь:

`infrastructure/.env`

`.env` добавлен в `.gitignore`, поэтому он не отправляется в GitHub.

Пароли от PostgreSQL и другие конфиденциальные настройки пока находятся в конфигурации проекта. Позже мы также вынесем их из Git в переменные окружения или другой механизм хранения секретов.

Для CI секреты, необходимые GitHub Actions, например `SONAR_TOKEN`, хранятся в **GitHub Secrets**.

Их реальные значения находятся локально в:

```text
infrastructure/.env
```

---

## Весь процесс целиком

```text
Developer
   ↓
feature branch
   ↓
Pull Request
   ↓
GitHub Actions CI
   ↓
Tests / Sonar / Build
   ↓
Merge → main
   ↓
Docker images
   ↓
GHCR
   ↓
Docker Compose
   ↓
Frontend + Backend + Infrastructure
```

Сейчас deployment на локальный компьютер выполняется вручную через Docker Compose.

Следующий этап проекта — перенести эту же схему на **Linux VPS** и автоматизировать deployment после успешного CI.

---

## Что студенту нужно запомнить?

Главная схема:

```text
Пишу код
 ↓
commit + push
 ↓
Pull Request
 ↓
CI должен быть зелёным
 ↓
Merge
 ↓
GitHub собирает Docker images
 ↓
GHCR
 ↓
Deployment
```

Если CI красный — открываем GitHub Actions, находим упавший job и читаем лог.

Главная идея CI/CD: **не собирать и проверять приложение вручную каждый раз, а сделать процесс повторяемым и автоматическим.**