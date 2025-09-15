# Inditex Pricing API

Backend demo project for the Inditex technical test, implemented with **Kotlin 1.9**, **Spring Boot 3.5**, **Java 17+**, and **Hexagonal Architecture + DDD**.

---

## 📦 Tech Stack

- **Kotlin 1.9**, **Java 21 or 17**
- **Spring Boot 3.5**
- **H2 in-memory database**
- **Spring Data JPA**
- **Springdoc OpenAPI + Swagger UI**
- **JUnit 5**, **Mockito-Kotlin**, **Rest-Assured**
- **Hexagonal Architecture (ports & adapters) + DDD**

---

## 🚀 How to Run

### 1. Build the project
```bash
mvn clean verify
```

### 2. Run the application
```bash
mvn spring-boot:run
```

The app starts on [http://localhost:8080](http://localhost:8080).

---

## 📖 API Documentation

### Swagger UI
Once the app is running, open:

- Swagger UI → [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI JSON → [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

The OpenAPI definition file is also available in the project root as `openapi-public.yaml`.

---

## 🗄 Database

The application uses an **in-memory H2 database**.  
Schema and demo data are auto-loaded from:

- `src/main/resources/schema.sql`
- `src/main/resources/data.sql`

### PRICES Table Example

| BRAND_ID | START_DATE          | END_DATE            | PRICE_LIST | PRODUCT_ID | PRIORITY | PRICE | CURR |
|----------|--------------------|--------------------|------------|------------|----------|-------|------|
| 1        | 2020-06-14 00:00:00 | 2020-12-31 23:59:59 | 1          | 35455      | 0        | 35.50 | EUR  |

---

## 🧪 Testing

### Run all tests
```bash
mvn test
```

### Run only unit tests
```bash
mvn test -Dtest=*Test
```

### Run only E2E tests
```bash
mvn test -Dtest=*E2ETest
```

All tests follow the `*Test` naming convention.

---

## ✅ CI/CD with GitHub Actions

This project includes a GitHub Actions workflow:

- On every **push/PR**:
  - Runs `mvn verify`
  - Compiles the app
  - Runs **all tests** (unit + E2E)

The build passes ✅ only if **all tests are green**.

---

## 🧩 Hexagonal Architecture Overview

- **Domain**
  - Pure business logic (`Price`, `PriceResolver`, etc.)
- **Inbound (Adapters)**
  - REST Controller (`PriceController`)
- **Outbound (Adapters)**
  - Persistence with JPA (`PriceRepositoryAdapter`, `JpaPriceEntity`)
- **Application**
  - Use cases (`GetApplicablePriceUseCase`)

This separation ensures a clean, testable, and maintainable design.

---

## 👨‍💻 Example Request

```bash
curl -X GET "http://localhost:8080/api/v1/prices?productId=35455&brandId=1&applicationDate=2020-06-14T10:00:00Z" \
  -H "accept: application/json"
```

### Example Response
```json
{
  "productId": "35455",
  "brandId": "1",
  "priceList": "1",
  "startDate": "2020-06-14T00:00:00Z",
  "endDate": "2020-12-31T23:59:59Z",
  "priority": 0,
  "price": {
    "amount": 35.50,
    "currency": "EUR"
  }
}
```

## 🐳 Docker Integration

### Prerequisites
- Docker installed on your system
- Application built with Maven

### Build and Run with Docker

1. **Build the application**
   ```bash
   mvn clean package
   ```

2. **Build the Docker image**
   ```bash
   docker build -t inditex-pricer .
   ```

3. **Run the container**
   ```bash
   docker run -p 8080:8080 inditex-pricer
   ```

The application will be available at [http://localhost:8080](http://localhost:8080).

### Docker Commands

| Command | Description |
|---------|-------------|
| `docker build -t inditex-pricer .` | Build the Docker image |
| `docker run -p 8080:8080 inditex-pricer` | Run the container |
| `docker run -d -p 8080:8080 inditex-pricer` | Run in detached mode |
| `docker ps` | List running containers |
| `docker logs <container-id>` | View container logs |
| `docker stop <container-id>` | Stop the container |

---

## 📝 Notes

- All dates are handled in **UTC** internally and serialized as `yyyy-MM-dd'T'HH:mm:ssZ`.
- The project uses a **pure domain model** (immutable) separated from JPA entities and DTOs.

## 🧪 Code Quality with SonarCloud

This project is integrated with [SonarCloud](https://sonarcloud.io) for static code analysis, code coverage, and technical debt tracking.

### ✅ Features

* **Automatic analysis on every push and PR** to the `master` branch.
* Uses **JaCoCo** for code coverage metrics.
* Runs through **GitHub Actions CI**.

### ⚙️ Configuration Details

* Default branch: `master`
* SonarCloud project key: `javalderas_inditex-pricing`
* The analysis is triggered from the GitHub Actions workflow: `.github/workflows/ci.yml`

> **Note:**
> SonarCloud expects a `main` branch by default. This project uses `master` as the default branch. Both GitHub and SonarCloud have been explicitly configured to treat `master` as the main branch.

### 🛠️ CI Step Overview

```yaml
- name: Build and analyze
  env:
    SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
  run: mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
       -Dsonar.projectKey=javalderas_inditex-pricing \
       -Dsonar.organization=javalderas \
       -Dsonar.host.url=https://sonarcloud.io
```

### 🔐 Authentication

The analysis uses a `SONAR_TOKEN` stored securely as a GitHub Actions secret. You can generate a token from your [SonarCloud Account Settings](https://sonarcloud.io/account/security/).

### 📊 Accessing Reports

All code quality reports and metrics are available at:
👉 [https://sonarcloud.io/dashboard?id=javalderas\_inditex-pricing](https://sonarcloud.io/dashboard?id=javalderas_inditex-pricing)
dffasdfas