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

**LoadTest MasterLab** — это **персональная демонстрационная версия** реального проекта, который я успешно внедрила и поддерживаю в своей компании.

### 🔐 Почему этот репозиторий

| Вопрос | Ответ |
|--------|-------|
| **Это реальный проект?** | ✅ Да, я работаю с этим в продакшене |
| **Почему не оригинал?** | 🔒 NDA — не могу раскрывать код компании |
| **Что отличается?** | Сохранена вся сложность (5 ручек в цепочке, 80/20 нагрузка), заменены только имена и данные |
| **Можно ли использовать?** | ✅ Да, это мой персональный код для портфолио |

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
Или запустить приложение по кнопке "run" возле `main` в классе `LoadTestApplication`
Приложение будет доступно на `http://localhost:8080`

### 🧪 Запуск тестов
Все тесты
```bash
mvn gatling:test
```
Конкретная симуляция
```bash
mvn gatling:test "-Dgatling.simulations=simulations.simulations.load.TransferLoadSimulation"
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
