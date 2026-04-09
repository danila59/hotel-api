
# Hotel API

RESTful API сервис для управления отелями.

---

## Технологии

* Java 21
* Spring Boot 3.5.13
* Spring Data JPA
* Spring Validation
* Liquibase (миграции БД)
* H2 Database (in-memory для разработки)
* PostgreSQL (через профили)
* MapStruct (маппинг Entity ↔ DTO)
* OpenAPI 3 / Swagger UI
* Maven
* JUnit 5 / Mockito

---

## Требования

* Java 21
* Maven 3.8+
* PostgreSQL 15+ (опционально)

---

## Документация

- JavaDoc: `target/site/apidocs/index.html` (после генерации)

## Установка и запуск

### Клонирование репозитория

```bash
git clone https://github.com/danila59/hotel-api.git
cd hotel-api
```

---

### Запуск с H2 (по умолчанию)

```bash
mvn spring-boot:run
```

Приложение будет доступно:

* Swagger UI: [http://localhost:8092/property-view/swagger-ui.html](http://localhost:8092/property-view/swagger-ui.html)
* H2 Console: [http://localhost:8092/h2-console](http://localhost:8092/h2-console)

---

### Запуск с PostgreSQL

Создайте базу данных:

```sql
CREATE DATABASE hoteldb;
```

Запуск:

```bash
mvn spring-boot:run "-Dspring-boot.run.profiles=postgres"
```

---

### Сборка и запуск JAR

```bash
mvn clean package
java -jar target/hotel-api-1.0.0.jar
```

---

## API Endpoints

Базовый путь: `/property-view`

| Метод | URL                      | Описание                    |
| ----- | ------------------------ | --------------------------- |
| GET   | `/hotels`                | Получить список всех отелей |
| GET   | `/hotels/{id}`           | Получить отель по ID        |
| GET   | `/search`                | Поиск отелей                |
| POST  | `/hotels`                | Создать отель               |
| POST  | `/hotels/{id}/amenities` | Добавить удобства           |
| GET   | `/histogram/{param}`     | Получить статистику         |

---

## Параметры поиска (`GET /search`)

| Параметр  | Тип    | Описание             |
| --------- | ------ | -------------------- |
| name      | String | Частичное совпадение |
| brand     | String | Точное совпадение    |
| city      | String | По городу            |
| country   | String | По стране            |
| amenities | String | По удобствам         |

---

## Параметры гистограммы (`GET /histogram/{param}`)

| Значение  | Описание                 |
| --------- | ------------------------ |
| brand     | Группировка по брендам   |
| city      | Группировка по городам   |
| country   | Группировка по странам   |
| amenities | Группировка по удобствам |

---

## Примеры запросов

### Получить все отели

```bash
curl http://localhost:8092/property-view/hotels
```

### Получить отель по ID

```bash
curl http://localhost:8092/property-view/hotels/1
```

### Поиск отелей в Минске

```bash
curl "http://localhost:8092/property-view/search?city=Minsk"
```

### Создать отель

```bash
curl -X POST http://localhost:8092/property-view/hotels \
  -H "Content-Type: application/json" \
  -d '{
    "name": "DoubleTree by Hilton Minsk",
    "brand": "Hilton",
    "address": {
      "houseNumber": "9",
      "street": "Pobediteley Avenue",
      "city": "Minsk",
      "country": "Belarus",
      "postCode": "220004"
    },
    "contacts": {
      "phone": "+375173098000",
      "email": "info@hilton.com"
    },
    "arrivalTime": {
      "checkIn": "14:00",
      "checkOut": "12:00"
    }
  }'
```

### Добавить удобства

```bash
curl -X POST http://localhost:8092/property-view/hotels/1/amenities \
  -H "Content-Type: application/json" \
  -d '["Free WiFi", "Parking", "Pool"]'
```

### Получить гистограмму

```bash
curl http://localhost:8092/property-view/histogram/city
```

---

## Примеры ответов

### GET /hotels

```json
[
  {
    "id": 1,
    "name": "DoubleTree by Hilton Minsk",
    "description": "Luxury hotel in city center",
    "address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
    "phone": "+375173098000"
  }
]
```

### GET /hotels/{id}

```json
{
  "id": 1,
  "name": "DoubleTree by Hilton Minsk",
  "description": "Luxury hotel in city center",
  "brand": "Hilton",
  "address": {
    "houseNumber": "9",
    "street": "Pobediteley Avenue",
    "city": "Minsk",
    "country": "Belarus",
    "postCode": "220004"
  },
  "contacts": {
    "phone": "+375173098000",
    "email": "info@hilton.com"
  },
  "arrivalTime": {
    "checkIn": "14:00",
    "checkOut": "12:00"
  },
  "amenities": ["Free WiFi", "Parking", "Pool"]
}
```

### GET /histogram/city

```json
{
  "Minsk": 5,
  "Moscow": 3,
  "Kiev": 2
}
```

---

## Структура проекта

```
src/
├── main/
│   ├── java/org/example/hotelapi/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── entity/
│   │   ├── dto/
│   │   ├── mapper/
│   │   └── config/
│   └── resources/
│       ├── application.yml
│       ├── application-h2.yml
│       ├── application-postgres.yml
│       └── db/changelog/
└── test/
```

---

## Тестирование

```bash
mvn test
```

Покрытие:

* Классы: 94%
* Методы: 99%
* Строки: 95%

---

## Валидация

| Поле               | Правило            |
| ------------------ | ------------------ |
| name               | 2–100 символов     |
| brand              | 2–50 символов      |
| phone              | формат +1234567890 |
| email              | валидный email     |
| postCode           | 5–6 цифр           |
| checkIn / checkOut | формат HH:MM       |

---

## Профили

| Профиль  | База данных | Команда                                                   |
| -------- | ----------- | --------------------------------------------------------- |
| default  | H2          | `mvn spring-boot:run`                                     |
| h2       | H2          | `mvn spring-boot:run "-Dspring-boot.run.profiles=h2"`       |
| postgres | PostgreSQL  | `mvn spring-boot:run "-Dspring-boot.run.profiles=postgres"` |

---

## Переменные окружения (PostgreSQL)

| Переменная  | Значение                                 |
| ----------- | ---------------------------------------- |
| DB_URL      | jdbc:postgresql://localhost:5432/hoteldb |
| DB_USERNAME | postgres                                 |
| DB_PASSWORD | postgres                                 |

---


## Использованные паттерны

* DTO - разделение слоев и скрытие внутренней структуры
* Mapper - конвертация между Entity и DTO
* Repository - абстракция доступа к данным
* Service Layer - бизнес-логика отдельно от контроллеров
* Dependency Injection - слабая связанность компонентов
* Builder - создание тестовых объектов
* Global Exception Handler - централизованная обработка ошибок

---
## Что можно улучшить

* Пагинация - в GET /hotels и GET /search для больших данных
* Кэширование
* Spring Security + JWT
* Docker / CI
* Индексы в БД
* Время заезда/выезда - хранить как LocalTime, а не String


## Контакты

danilapavlichenko6@gmail.com

---

