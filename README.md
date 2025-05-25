# La pause clope

[![Spring Test](https://github.com/LaPauseClope/pause-clope-server/actions/workflows/maven.yml/badge.svg)](https://github.com/LaPauseClope/pause-clope-server/actions/workflows/maven.yml)
[![Latest Release](https://img.shields.io/github/v/release/LaPauseClope/pause-clope-server)](https://github.com/LaPauseClope/pause-clope-server/releases)

Link to main [documentation](https://www.youtube.com/watch?v=dQw4w9WgXcQ).
A simple Spring Boot application that simulates a Cookie Clicker game, with PostgreSQL integration.

---

## 🛠️ Development Environment Setup

### 🔧 Prerequisites

- Java 21 (Open jdk)
- Maven
- IntelliJ IDEA (recommended)
- PostgreSQL (can be run using Docker)

---

## 🐳 PostgreSQL Setup with Docker

Run the following command to start a PostgreSQL container:

```bash
docker run --name postgres-container \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -e POSTGRES_DB=cookieclicker \
  -p 5432:5432 \
  -d postgres
```

----
To stop and remove the container:

```bash
docker stop postgres-container
docker rm postgres-container
```

## ⚙️ Application Configuration

Edit your application.yaml in src/main/resources:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cookieclicker
    username: postgres
    password: password
  jpa:
    hibernate:
      ddl-auto: update
      show-sql: true
  profiles:
    active: dev

  server:
    port: 8080
```

Change active profile depending on configuration

---

## 🧱 Build and Run the App

### 🔨 Build with Maven

```bash
./mvnw clean install
```

▶️ Run with Spring Boot

```bash
./mvnw spring-boot:run
```

Or directly with the jar:

```bash
java -jar target/cookie-clicker-0.0.1-SNAPSHOT.jar
```

```bash
./mvnw test
```

---

## Postman testing

Link : [click here](https://app.getpostman.com/join-team?invite_code=8cbac6d126d4553a0384e4f33e19a25b964669e660f47611d5235e16fc5ea495&target_code=a3f18daaa9304aef43bcea35df2fe791)
Edit variables in "Environment" space in postman to match with your configuration
