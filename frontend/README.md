# Training Center Platform — Frontend

Frontend для учебного проекта **Training Center Platform**.

Разработан на **React + Vite** и взаимодействует с backend-микросервисами через REST API.

## Стек

- React
- Vite
- JavaScript
- React Router
- Axios
- CSS

## Возможности

Интерфейс разделён по ролям:

### STUDENT
- просмотр и редактирование профиля;
- просмотр доступных курсов;
- запись на курс;
- просмотр своих записей;
- создание и просмотр платежей;
- просмотр документов.

### TEACHER
- просмотр профиля;
- просмотр назначенных курсов;
- просмотр студентов курса.

### ADMIN
- управление пользователями и ролями;
- создание, редактирование и удаление курсов;
- назначение преподавателя на курс;
- просмотр записей студентов;
- просмотр платежей и документов.

---

# Запуск frontend

## 1. Установить Node.js

Для запуска необходим **Node.js** с npm.

Проверить установку:

```bash
node -v
npm -v
```

Если команды выводят версии — всё готово.

## 2. Перейти в папку frontend

Из корня проекта:

```bash
cd frontend
```

## 3. Установить зависимости

При первом запуске или после изменения `package.json`:

```bash
npm install
```

Будет создана папка:

```text
node_modules/
```

Её в Git добавлять не нужно.

## 4. Запустить frontend

```bash
npm run dev
```

Vite выведет адрес приложения, обычно:

```text
http://localhost:5173
```

Открыть его в браузере.

Для остановки:

```text
Ctrl + C
```

---

# Backend

Для полноценной работы frontend должны быть запущены необходимые backend-сервисы:

```text
auth-service
user-service
course-service
enrollment-service
notification-service
payment-service
document-service
discovery-service
```

Также должны работать PostgreSQL и необходимая инфраструктура проекта.

Если, например, `payment-service` не запущен, раздел платежей работать не будет.

---

# Авторизация

Используется JWT.

После входа frontend получает Access Token и определяет роль пользователя:

```text
STUDENT
TEACHER
ADMIN
```

От роли зависит доступное меню и страницы.

После изменения роли пользователя рекомендуется выйти из аккаунта и войти заново.

---

# Быстрый запуск после клонирования

```bash
git clone <URL_РЕПОЗИТОРИЯ>
cd training-center-platform/frontend
npm install
npm run dev
```

Затем открыть:

```text
http://localhost:5173
```

---

# После git pull

Получить изменения:

```bash
git pull
```

Если изменились frontend-зависимости:

```bash
cd frontend
npm install
```

Запустить:

```bash
npm run dev
```

---

# Частые проблемы

### `npm` не найден

Проверить:

```bash
node -v
npm -v
```

Если команды не работают — необходимо установить Node.js.

### `ERR_CONNECTION_REFUSED`

Обычно нужный backend-сервис не запущен.

Проверить сервисы и их порты.

### Frontend возвращает 401

Возможные причины:

- пользователь не авторизован;
- JWT отсутствует или истёк;
- у пользователя нет нужной роли.

Попробовать выйти из аккаунта и войти снова.

---

# Основной сценарий

```text
Регистрация / Вход
        ↓
Просмотр курсов
        ↓
Запись на курс
        ↓
Enrollment
        ↓
Мои записи
        ↓
Оплата курса
        ↓
Payment
```

Следующий этап проекта:

```text
Payment
   ↓
PENDING
   ↓
COMPLETED / FAILED
   ↓
Kafka
   ↓
Document Service
   ↓
PDF
   ↓
MinIO
   ↓
Notification Service
   ↓
Email
```