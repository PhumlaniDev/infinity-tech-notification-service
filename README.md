
# 📬 Notification Service
A standalone microservice for sending email, SMS, and push notifications triggered by events in other microservices (like order or auth service). Built with Spring Boot, Keycloak security, PostgreSQL, Thymeleaf email templating, and Docker.

## 🧭 Table of Contents
- [✅ Features](#-features)
- [🧱 Architecture](#-architecture)
- [🛠️ Tech Stack](#-tech-stack)
- [📮 API Endpoints](#-api-endpoints)
- [🗃️ Database Schema](#-database-schema)
- [🚀 Getting Started](#-getting-started)
- [✉️ Email Templates](#-email-templates)
- [🌱 Future Enhancements](#-future-enhancements)
- [🤝 Contributing](#-contributing)
- [🔐 Security](#-security)
- [📄 License](#-license)

## ✅ Features
- Secure JWT-based authentication via Keycloak
- Sends:
  - Order confirmation emails
  - Password reset and account verification emails
  - SMS and push notifications
- Thymeleaf-based email templating
- Logs all notifications in PostgreSQL using Flyway migrations
- Easily extensible to Kafka or RabbitMQ
- Dockerized and Eureka/Gateway-ready
## 🧱 Architecture

```text
+-------------+      HTTPS       +---------------+      publishEvent      +-------------------+
| API Gateway |  ─────────────▶  | Order Service |  ───────────────────▶  | NotificationService|
+-------------+                  +---------------+                         +-------------------+
                                                │
                                                ▼
                                        +----------------+
                                        | PostgreSQL DB  |
                                        +----------------+
                                                ▲
                                                │
                                      +------------------+
                                      |   SMTP / Mailgun  |
                                      +------------------+
```
## 🛠️ Tech Stack

| Component  | Technology                 |
| ---------- | -------------------------- |
| Language   | Java 17                    |
| Framework  | Spring Boot 3              |
| Security   | Spring Security + Keycloak |
| Email      | Spring Mail + Thymeleaf    |
| DB         | PostgreSQL                 |
| Migrations | Flyway                     |
| Auth       | OAuth2 Bearer Token        |
| Containers | Docker + Compose           |

## 📮 API Endpoints

| Method | Path                                         | Purpose                       |
| ------ | -------------------------------------------- | ----------------------------- |
| POST   | `/api/v1/notifications/order`                | Send order confirmation email |
| POST   | `/api/v1/notifications/email`                | Send general email            |
| POST   | `/api/v1/notifications/sms`                  | Send SMS                      |
| POST   | `/api/v1/notifications/push`                 | Send push notification        |
| POST   | `/api/v1/notifications/password-reset`       | Send password reset email     |
| POST   | `/api/v1/notifications/account-verification` | Send verification email       |
| POST   | `/api/v1/notifications/order-status`         | Shipped/Delivered/Cancelled   |
| POST   | `/api/v1/notifications/refund`               | Notify refund processed       |
| POST   | `/api/v1/notifications/cart-reminder`        | Abandonment reminder          |

## 🗃️ Database Schema

```sql
CREATE TABLE notification_log (
  id SERIAL PRIMARY KEY,
  user_id VARCHAR(255),
  type VARCHAR(50),
  channel VARCHAR(100),
  status VARCHAR(50),
  destination VARCHAR(255),
  content TEXT,
  sent_at TIMESTAMP,
  failed_at TIMESTAMP,
  error_message TEXT
);
```


## 🚀 Getting Started

✅ Prerequisites
- Java 17+

- Maven

- Docker + Docker Compose

- Keycloak running on http://localhost:8080/
## ▶️ Run with Docker

```bash
# Build the jar
mvn clean package -DskipTests
```

```bash
# Start the service with DB
docker-compose up --build
```


## ✉️ Email Templates
Location: src/main/resources/templates/

Example: order-confirmation.html

```html
<p>Hello,</p>
<p>Your order <strong>#<span th:text="${orderId}"></span></strong> has been placed!</p>
<p>Total: $<span th:text="${total}"></span></p>
```
Variables like orderId and total are injected using Thymeleaf.

## Future Enhancements
🌱 Future Enhancements
* ✅ Kafka support for consuming OrderPlacedEvent

* ✅ Email queue + retry mechanism

* ✅ Push via Firebase Cloud Messaging (FCM)

* ✅ Admin UI for notification logs

* ✅ Slack/Discord notification integration

* ✅ Monitoring with Prometheus/Grafana
## 🤝 Contributing
Pull requests are welcome! Open issues for improvements or bugs.


## 🔐 Security
This service validates Bearer tokens via Keycloak:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:8080/realms/<your-realm>

```

## 📄 License
MIT License — use it freely and improve it! 🚀