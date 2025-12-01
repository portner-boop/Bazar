## Bazar API

Bazar — это платформа для сбыта товаров, объединяющая заказчиков и исполнителей. Платформа поддерживает создание задач на сбыт партий товаров, систему откликов (claims), безопасные сделки через эскроу и управление пользователями с ролями.

### Основные возможности

- ✅ Управление категориями товаров
- ✅ Создание и управление задачами на сбыт
- ✅ Система откликов исполнителей
- ✅ Загрузка изображений для задач (MinIO)
- ✅ Управление пользователями и ролями (USER, ADMIN, SUPPORT)
- ✅ Пагинация для всех списков
- ✅ Кэширование через Redis
- ✅ Фильтрация и поиск
- ✅ Оптимизация для масштабирования до 100k пользователей

### Технологический стек

- **Backend**: Spring Boot 3.5.7, Java 17
- **База данных**: PostgreSQL 15
- **Кэш**: Redis 7
- **Хранилище**: MinIO (S3-совместимое)
- **Документация**: Swagger/OpenAPI

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

### Документация

- **Полная документация API**: [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`
- **Коллекция Bruno для тестирования**: [bruno-collection.json](bruno-collection.json)

### Основные REST-эндпоинты (v1)

Базовый префикс: `/api/v1`.

- **Категории** (`/categories`)
  - `POST /api/v1/categories` — создать категорию
  - `GET /api/v1/categories` — список категорий
  - `GET /api/v1/categories/{id}` — получить категорию по id
  - `PUT /api/v1/categories/{id}` — обновить категорию
  - `DELETE /api/v1/categories/{id}` — удалить категорию

- **Задания** (`/tasks`)
  - `POST /api/v1/tasks` — создать задание
  - `GET /api/v1/tasks?page=0&size=20` — список заданий (с пагинацией)
  - `GET /api/v1/tasks/{id}` — получить задание
  - `PUT /api/v1/tasks/{id}` — обновить задание
  - `GET /api/v1/tasks/filter` — фильтрация по `region`, `status`, `categoryId`, `telegramUserId`

- **Изображения задач** (`/tasks/{taskId}/images`)
  - `POST /api/v1/tasks/{taskId}/images` — загрузить изображения
  - `GET /api/v1/tasks/{taskId}/images` — получить изображения задачи
  - `DELETE /api/v1/tasks/images/{imageId}` — удалить изображение

- **Заявки (Claim)** (`/claims`)
  - `POST /api/v1/claims` — создать заявку на задание
  - `GET /api/v1/claims?page=0&size=20` — список заявок (с пагинацией и фильтрацией)
  - `GET /api/v1/claims/{id}` — получить заявку
  - `PUT /api/v1/claims/{id}` — обновить заявку
  - `DELETE /api/v1/claims/{id}` — удалить заявку

- **Пользователи Telegram** (`/users`)
  - `GET /api/v1/users?page=0&size=20` — список пользователей (с пагинацией)
  - `GET /api/v1/users/{id}` — получить пользователя
  - `GET /api/v1/users/telegram/{telegramUserId}` — получить пользователя по Telegram ID
  - `PUT /api/v1/users/{id}` — обновить пользователя
  - `PUT /api/v1/users/{targetUserId}/role?adminId=1` — изменить роль пользователя (только ADMIN)

### Авторизация через Telegram

- Telegram-бот регистрируется через конфигурацию в `TelegramConfig` и `TelegramBotRegistrar`.
- Команда `/register` в боте:
  - создает пользователя, если Telegram ID еще не зарегистрирован;
  - возвращает сообщение о статусе регистрации.

### Модель домена и статусы

- **TelegramUser** — пользователь системы
  - Связан с `Task` (созданные задания) и `Claim` (заявки)
  - Ключевое поле: `telegramId` — уникальный идентификатор пользователя в Telegram
  - Роли: `USER`, `ADMIN`, `SUPPORT`

- **Category** — категория товаров
  - Классифицирует задания (`Task`), связь один-ко-многим

- **Task** — задача на сбыт товара
  - Ссылка на `Category` и `TelegramUser` (кто создал)
  - Может иметь несколько изображений (`TaskImage`)
  - Поля:
    - `rewardType`: `FIXED_AMOUNT`, `PERCENTAGE`, `MIXED`
    - `status`: `NEW`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`
    - `escrowStatus`: `NOT_REQUIRED`, `PENDING`, `FUNDED`, `RELEASED`, `DISPUTED`

- **Claim** — отклик исполнителя на задачу
  - Связана с `Task` и `TelegramUser` (кто подал заявку)
  - `status`: `PENDING`, `APPROVED`, `REJECTED`

- **TaskImage** — изображение задачи
  - Хранится в MinIO (S3-совместимое хранилище)
  - Связана с `Task`

### Оптимизация и производительность

Все статусы и типы вознаграждения вынесены в `enum`, что:
- запрещает записывать произвольные строки в БД;
- явно ограничивает допустимые состояния и делает API самодокументируемым (видно в Swagger).

### Обработка ошибок и валидация

- Используется **Bean Validation** (`jakarta.validation`) для DTO и сущностей
- Все ошибки валидации и доменные исключения обрабатываются в `GlobalExceptionHandler` и отдаются в едином формате `ApiErrorResponse`

### Тестирование

Импортируйте коллекцию `bruno-collection.json` в [Bruno](https://www.usebruno.com/) для тестирования всех эндпоинтов API.


