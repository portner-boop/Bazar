## Bazar API

Bazar — это сервис с Telegram-авторизацией для работы с категориями, заданиями и заявками.

### Запуск

- **Через Maven**
  - Убедись, что Postgres запущен и совпадает конфиг в `application.yml`.
  - Запуск приложения:

```bash
mvn spring-boot:run
```

- **Через Docker Compose**

```bash
docker-compose up --build
```

Приложение по умолчанию поднимается на `http://localhost:8080`.

### Swagger / OpenAPI

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

Через Swagger UI можно выполнять все запросы и смотреть схемы DTO.

### Основные REST-эндпоинты (v1)

Базовый префикс: `/api/v1`.

- **Категории** (`/categories`)
  - `POST /api/v1/categories` — создать категорию.
  - `GET /api/v1/categories` — список категорий.
  - `GET /api/v1/categories/{id}` — получить категорию по id.
  - `PUT /api/v1/categories/{id}` — обновить категорию.
  - `DELETE /api/v1/categories/{id}` — удалить категорию.

- **Задания** (`/tasks`)
  - `POST /api/v1/tasks` — создать задание.
  - `GET /api/v1/tasks` — список заданий.
  - `GET /api/v1/tasks/{id}` — получить задание.
  - `PUT /api/v1/tasks/{id}` — обновить задание.
  - `GET /api/v1/tasks/filter` — фильтрация по `region`, `status`, `categoryId`, `telegramUserId`.

- **Заявки (Claim)** (`/claims`)
  - `POST /api/v1/claims` — создать заявку на задание.
  - `GET /api/v1/claims` — список заявок с фильтрацией по `status`, `taskId`, `telegramUserId`.
  - `GET /api/v1/claims/{id}` — получить заявку.
  - `PUT /api/v1/claims/{id}` — обновить заявку.
  - `DELETE /api/v1/claims/{id}` — удалить заявку.

- **Пользователи Telegram** (`/users`)
  - `GET /api/v1/users` — список Telegram-пользователей.
  - `GET /api/v1/users/{id}` — получить пользователя.
  - `PUT /api/v1/users/{id}` — обновить основные поля (имя, фамилия, email).

### Авторизация через Telegram

- Telegram-бот регистрируется через конфигурацию в `TelegramConfig` и `TelegramBotRegistrar`.
- Команда `/register` в боте:
  - создает пользователя, если Telegram ID еще не зарегистрирован;
  - возвращает сообщение о статусе регистрации.

### Обработка ошибок и валидация

- Используется **Bean Validation** (`jakarta.validation`) для DTO и сущностей.
- Все ошибки валидации и доменные исключения обрабатываются в `GlobalExceptionHandler` и отдаются в едином формате `ApiErrorResponse`.


