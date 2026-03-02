# LoadTest MasterLab

[![Gatling](https://img.shields.io/badge/Gatling-3.10.2-blue.svg)](https://gatling.io/)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-red.svg)](https://maven.apache.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)

> **Репозиторий для практики нагрузочного тестирования на Gatling (Java DSL)**

## 📚 Документация

- [README.md](README.md) — основная документация
- [docs/TEST_DIARY.md](docs/TEST_DIARY.md) — 📔 Дневник нагрузочного тестирования (кейс Load → Spike)

---

## 📋 Оглавление

- [О проекте](#-о-проекте)
- [Технологии](#-технологии)
- [Структура проекта](#-структура-проекта)
- [Быстрый старт](#-быстрый-старт)
- [Запуск тестов](#-запуск-тестов)
- [Результаты тестирования](#-результаты-тестирования)
- [Архитектура](#-архитектура)
- [Расширение](#-расширение)
- [Лицензия](#-лицензия)

---

## 📖 О проекте

**LoadTest MasterLab** — это учебный проект для освоения нагрузочного тестирования REST API с использованием **Gatling** на **Java DSL**.

### Цели проекта

- ✅ Освоить Gatling Java API для написания тестов как код
- ✅ Изучить профессиональную архитектуру проекта для нагрузочных тестов
- ✅ Получить опыт тестирования реального Spring Boot приложения
- ✅ Создать портфолио для позиции **QA Backend / Lead QA**

### Ключевые возможности

| Возможность | Описание |
|------------|----------|
| **Java DSL** | Тесты пишутся на Java, а не на Scala |
| **Enterprise-архитектура** | Разделение по слоям: config, scenarios, assertions |
| **Динамические данные** | Gatling EL для подстановки значений (`#{postIds.random()}`) |
| **Глобальные assertions** | Проверки для интеграции в CI/CD |
| **Конфигурация** | Вынос эндпоинтов в `.properties` файлы |
| **Отчётность** | HTML-отчёты с графиками и статистикой |

---

## 🛠 Технологии

| Технология | Версия | Назначение |
|-----------|--------|------------|
| **Gatling** | 3.10.2 | Нагрузочное тестирование |
| **Java** | 17 | Язык написания тестов |
| **Maven** | 3.8+ | Сборка и зависимости |
| **Spring Boot** | 3.3.5 | Тестовое приложение |
| **Lombok** | 1.18.34 | Уменьшение бойлерплейта |
| **H2 Database** | 2.2.224 | In-memory база данных |

---

## 📁 Структура проекта
```text
loadtest-masterlab/
├── src/main/
│ ├── java/simulations/
│ │ ├── core/ # Базовые классы (BaseSimulation)
│ │ ├── config/ # Конфигурация (ApiConfig, ApiEndpoints)
│ │ ├── protocol/ # HTTP протоколы (HttpProtocolFactory)
│ │ ├── scenarios/ # Сценарии по доменам
│ │ │ ├── post/ # Сценарии для постов
│ │ │ └── user/ # Сценарии для пользователей
│ │ ├── assertions/ # Глобальные assertions
│ │ └── simulations/ # Точки входа
│ │ ├── smoke/ # Дымовые тесты
│ │ ├── load/ # Нагрузочные тесты
│ │ └── endurance/ # Длительные тесты
│ └── resources/
│ ├── config/ # .properties файлы
│ ├── bodies/ # JSON тела запросов
│ └── data/ # CSV данные (Feeders)
├── app/ # Spring Boot приложение для тестов
├── pom.xml # Maven конфигурация
├── gatling.conf # Настройки Gatling
└── README.md # Документация
```


---

## 🚀 Быстрый старт

### Требования

- Java 17+
- Maven 3.8+
- IntelliJ IDEA (рекомендуется)

### Установка

```bash
# Клонировать репозиторий
git clone https://github.com/victoriasemkina/loadtest-masterlab.git
cd loadtest-masterlab

# Собрать проект
mvn clean compile
```

### Запуск тестового приложения
```bash
# Терминал 1: Запустить Spring Boot приложение
cd app
mvn spring-boot:run
```
Приложение будет доступно на `http://localhost:8080`

### 🧪 Запуск тестов
Все тесты
```bash
mvn gatling:test
```
Конкретная симуляция
```bash
mvn gatling:test "-Dgatling.simulations=simulations.simulations.load.ApiLoadSimulation"
```
Проверка API вручную
```bash
# PowerShell
Invoke-RestMethod -Uri "http://localhost:8080/api/posts" -Method Get

# Создать пост
Invoke-RestMethod -Uri "http://localhost:8080/api/posts" -Method Post `
  -ContentType "application/json" `
  -Body '{"title": "Test", "body": "Test Body", "userId": 1}'
```

### Отчёт Gatling
После запуска тестов отчёт сохраняется автоматически: `file:///C:/Users/{user}/Downloads/loadtest-masterlab/target/gatling/gatling-{timestamp}/index.html`

---

## 🏗 Архитектура
Слои проекта
```text
┌─────────────────────────────────────────────────────────┐
│                    Simulations (Entry Points)           │
│              smoke/, load/, endurance/                  │
├─────────────────────────────────────────────────────────┤
│                    Scenarios (Business Logic)           │
│              post/, user/, comment/                     │
├─────────────────────────────────────────────────────────┤
│                    Protocol (HTTP Configuration)        │
│              HttpProtocolFactory                        │
├─────────────────────────────────────────────────────────┤
│                    Config (Settings & Endpoints)        │
│              ApiConfig, ApiEndpoints                    │
├─────────────────────────────────────────────────────────┤
│                    Core (Base Classes)                  │
│              BaseSimulation                             │
└─────────────────────────────────────────────────────────┘
```
## 🔑 Ключевые классы проекта

| Класс | Путь | Назначение |
|-------|------|-----------|
| `ApiConfig.java` | `src/main/java/simulations/config/` | Универсальный класс для чтения `.properties` файлов. Предоставляет методы `getPropertyByKey()`, `getIntPropertyByKey()` для получения конфигурации |
| `ApiEndpoints.java` | `src/main/java/simulations/config/` | Enum всех эндпоинтов API. Каждый эндпоинт имеет ключ для получения значения из конфига. Методы: `getValue()`, `getIntValue()` |
| `HttpProtocolFactory.java` | `src/main/java/simulations/protocol/` | Фабрика для создания HTTP протоколов. Настраивает базовый URL, заголовки, таймауты через `gatling.conf` |
| `BaseSimulation.java` | `src/main/java/simulations/core/` | Базовый класс для всех симуляций. Предоставляет метод `httpProtocol()` для наследования |
| `PostScenarios.java` | `src/main/java/simulations/scenarios/post/` | Сценарии для работы с постами: `getAllPosts()`, `getPostById()`, `createPost()`, `fullPostLifecycle()` |
| `UserScenarios.java` | `src/main/java/simulations/scenarios/user/` | Сценарии для работы с пользователями: `getAllUsers()`, `getUserById()`, `fullUserLifecycle()` |
| `GlobalAssertions.java` | `src/main/java/simulations/assertions/` | Глобальные assertions для CI/CD. Метод `standardAssertions()` возвращает массив проверок (response time, failed requests) |
| `ApiLoadSimulation.java` | `src/main/java/simulations/simulations/load/` | Точка входа для нагрузочного теста. Определяет сценарий, инъекцию пользователей (ramp-up, steady, ramp-down), assertions |
| `SmokeSimulation.java` | `src/main/java/simulations/simulations/smoke/` | Точка входа для дымовых тестов. Быстрая проверка работоспособности API (1-2 минуты) |
| `LoadTestApplication.java` | `app/src/main/java/.../loadtest/` | Главный класс Spring Boot приложения. Запускает тестовый API на порту 8080 |
| `PostController.java` | `app/src/main/java/.../loadtest/controller/` | REST контроллер для работы с постами. Endpoints: GET, POST `/api/posts` |
| `Post.java` | `app/src/main/java/.../loadtest/model/` | JPA Entity для таблицы posts. Поля: id, title, body, userId |
| `PostRepository.java` | `app/src/main/java/.../loadtest/service/` | Spring Data JPA репозиторий для CRUD операций с постами |

---

## 🔧 Расширение
### Добавить новый сценарий
1. Создать класс в `src/main/java/simulations/scenarios/{domain}/`
2. Добавить методы `ChainBuilder` для сценариев 
3. Использовать в симуляции через `.exec()`
4. Добавить новый эндпоинт 
5. Добавить ключ в `api-endpoints.properties` 
6. Добавить enum в `ApiEndpoints.java` 
7. Использовать в сценарии через `ApiEndpoints.ENDPOINT.getValue()`

### Добавить Feeders (CSV данные)
```java
// В сценарии
.exec(
    http("POST /posts")
        .post("/api/posts")
        .body(ElFileBody("bodies/post/create-post.json")).asJson()
        .check(status().is(201))
)
.feed(CsvFeederBuilder.csv("data/posts.csv").circular())
```