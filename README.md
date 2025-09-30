# Система управления банковскими картами

[![Java](https://img.shields.io/badge/Java-21%2B-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-green)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue)](https://www.docker.com/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)](https://www.mysql.com/)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-yellow)](https://swagger.io/)

Backend-приложение на **Java Spring Boot** для управления банковскими картами.  
Поддерживает функционал для администратора и пользователя с безопасной аутентификацией через JWT.

---

## 📁 Структура проекта

- `controller/` — REST контроллеры
- `service/` — Бизнес-логика
- `dto/` — Объекты передачи данных (request/response)
- `repository/` — Работа с базой данных
- `config/` — Конфигурации Spring и OpenAPI
- `docs/` — OpenAPI YAML документация
- `db/migration/` — Миграции Liquibase
---

## 📝 Функционал

### Администратор
- Создание, блокировка, активация, удаление карт
- Управление пользователями
- Просмотр всех карт

### Пользователь
- Просмотр своих карт (с пагинацией и поиском)
- Запрос на блокировку карты
- Переводы между своими картами
- Просмотр баланса

### Атрибуты карты
- Номер карты (маскирован `**** **** **** 1234`)
- Владелец
- Срок действия
- Статус: Активна, Заблокирована, Истек срок
- Баланс

---

**Swagger UI:** `/openapi/swagger-ui.html`  
**API Docs:** `/openapi/api-docs.yaml`

---

## 🗄 База данных
- MySQL
- Управление через миграции Liquibase (`src/main/resources/db/migration`)
- Docker volume для постоянного хранения данных

---

## 🛠 Технологии
- Java 21
- Spring Boot, Spring Security, Spring Data JPA
- JWT аутентификация
- MySQL
- Liquibase
- Docker & Docker Compose
- Swagger / OpenAPI 3.x

---

## 🚀 Запуск локально

1. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/Armen23023/bank_rest.git
   cd bank_rest
   
2. Настройте application.yml для базы данных и JWT.

3. Запустите приложение через Maven/Gradle или Docker Compose:

   ```bash
    docker-compose up --build
   
  .MySQL будет доступен на localhost:3307 (если на хосте 3306 уже занят).

  .Spring Boot приложение будет доступно на localhost:8080.

  .Swagger UI: http://localhost:8080/openapi/swagger-ui/index.html#/

4. Остановка сервисов:

    ```bash
    docker-compose down
