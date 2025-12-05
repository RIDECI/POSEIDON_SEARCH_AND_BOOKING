# 📚 Poseidon - Search and Booking Backend

This module is designed to manage travel search and booking operations within the RideCi ecosystem: it searches for available travels, creates bookings, manages booking status (e.g., PENDING, CONFIRMED, CANCELLED), handles seat availability, and publishes booking events to other microservices via RabbitMQ.

## 👥 Developers

- Deisy Lorena Guzman Cabrales
- Diego Fernando Chavarro Castillo
- Oscar Andres Sanchez Porras
- Samuel Leonardo Albarrachin Vergara
- Sergio Alejandro Idarraga

---

## 📑 Content Table

1. [Project Architecture](#-project-architecture)
    - [Hexagonal Structure](#-clean---hexagonal-structure)
2. [API Documentation](#-api-endpoints)
    - [Endpoints](#-api-endpoints)
3. [Input & Output Data](#input-and-output-data)
4. [Microservices Integration](#-connections-with-other-microservices)
5. [Technologies](#technologies)
6. [Branch Strategy](#-branches-strategy--structure)
7. [System Architecture & Design](#-system-architecture--design)
8. [Getting Started](#-getting-started)
9. [Testing](#-testing)

---

## 🏛️ Project Architecture

The Poseidon - Search and Booking backend follows a decoupled hexagonal / clean architecture combined with CQRS (Command Query Responsibility Segregation) that isolates business logic from infrastructure and external services by splitting the code into multiple components:

* **🧠 Domain (Core)**: Contains the booking entities (`Booking`), value objects, enums (`BookingStatus`) and the core business rules including seat availability validation.

* **🎯 Ports (Interfaces)**: Interfaces that define which operations the domain can perform (use cases exposed as input ports like `CreateBookingCommand`, `GetBookingQuery`) and required external interactions as output ports.

* **🔌 Adapters (Infrastructure)**: Implementations of the ports that connect the domain with technologies such as HTTP controllers, MongoDB persistence and RabbitMQ event publishing.

* **📡 Event-Driven**: Uses RabbitMQ (CloudAMQP) to publish booking events (`BookingCreatedEvent`, `BookingUpdatedEvent`, `BookingCancelledEvent`) for asynchronous communication with other microservices.

Main benefits of this architecture:

* ✅ **Separation of Concerns:** Clear boundaries between business logic and infrastructure.
* ✅ **Maintainability:** Easier to modify or replace specific components.
* ✅ **Scalability:** Components can evolve and scale independently.
* ✅ **Testability:** The domain can be tested in isolation without database or message broker (86% test coverage achieved).
* ✅ **Event-Driven Communication:** Decoupled microservices integration via asynchronous messaging.

## 📂 Clean - Hexagonal Structure

```
📂 poseidon_search_and_booking
 ┣ 📂 src/
 ┃ ┣ 📂 main/
 ┃ ┃ ┣ 📂 java/
 ┃ ┃ ┃ ┗ 📂 edu/dosw/rideci/
 ┃ ┃ ┃   ┣ 📄 PoseidonSearchAndBookingApplication.java
 ┃ ┃ ┃   ┣ 📂 domain/
 ┃ ┃ ┃   ┃ ┗ 📂 model/            # 🧠 Domain models (Booking)
 ┃ ┃ ┃   ┣ 📂 application/
 ┃ ┃ ┃   ┃ ┣ 📂 event/           # 📡 Domain events (BookingCreatedEvent, BookingUpdatedEvent)
 ┃ ┃ ┃   ┃ ┣ 📂 mappers/         # 🔄 MapStruct mappers (BookingMapper, BookingMapperInitial)
 ┃ ┃ ┃   ┃ ┣ 📂 ports/
 ┃ ┃ ┃   ┃ ┃ ┗ 📂 in/             # 🎯 Input ports (CQRS commands & queries)
 ┃ ┃ ┃   ┃ ┗ 📂 service/          # ⚙️ Use case implementations (BookingService)
 ┃ ┃ ┃   ┣ 📂 exceptions/         # ❗ Custom domain exceptions (BookingNotFoundException, InsufficientSeatsException)
 ┃ ┃ ┃   ┣ 📂 infrastructure/
 ┃ ┃ ┃   ┃ ┣ 📂 adapters/        # 🔌 RabbitMQ event publisher adapter
 ┃ ┃ ┃   ┃ ┣ 📂 config/           # ⚙️ Spring / Infra configuration (RabbitMQ, MongoDB)
 ┃ ┃ ┃   ┃ ┣ 📂 controllers/
 ┃ ┃ ┃   ┃ ┃ ┣ 📄 BookingController.java   # 🌐 REST controllers
 ┃ ┃ ┃   ┃ ┃ ┗ 📂 dto/                   # 📨 Request / Response DTOs
 ┃ ┃ ┃   ┃ ┗ 📂 persistence/
 ┃ ┃ ┃   ┃   ┣ 📂 entity/          # 🗄️ MongoDB documents (BookingDocument)
 ┃ ┃ ┃   ┃   ┗ 📂 repository/      # 🧰 Spring Data repositories & adapters
 ┃ ┃ ┃   ┃     ┣ 📄 BookingRepository.java
 ┃ ┃ ┃   ┃     ┗ 📄 BookingRepositoryAdapter.java
 ┃ ┃ ┗ 📂 resources/
 ┃ ┃   ┗ 📄 application.properties
 ┣ 📂 test/
 ┃ ┣ 📂 java/
 ┃ ┃ ┗ 📂 edu/dosw/rideci/
 ┃ ┃   ┣ 📂 config/
 ┃ ┃   ┃ ┗ 📄 TestConfig.java
 ┃ ┃   ┣ 📂 unit/
 ┃ ┃   ┃ ┣ 📂 application/
 ┃ ┃   ┃ ┃ ┣ 📄 BookingServiceTest.java (14 tests)
 ┃ ┃   ┃ ┃ ┣ 📄 RabbitMQEventPublisherTest.java (2 tests)
 ┃ ┃   ┃ ┃ ┣ 📄 MappersIntegrationTest.java (6 tests)
 ┃ ┃   ┃ ┃ ┣ 📄 DTOsTest.java (5 tests)
 ┃ ┃   ┃ ┃ ┗ 📄 DTOsInfrastructureTest.java (8 tests)
 ┃ ┃   ┃ ┣ 📂 domain/
 ┃ ┃   ┃ ┃ ┣ 📄 BookingDomainTest.java (6 tests)
 ┃ ┃   ┃ ┃ ┗ 📄 BookingDocumentTest.java (6 tests)
 ┃ ┃   ┃ ┗ 📂 infrastructure/
 ┃ ┃   ┃   ┣ 📄 BookingControllerTest.java (10 tests)
 ┃ ┃   ┃   ┣ 📄 BookingRepositoryAdapterTest.java (14 tests)
 ┃ ┃   ┃   ┗ 📄 ExceptionsTest.java (4 tests)
 ┃ ┃   ┣ 📂 e2e/
 ┃ ┃   ┗ 📂 load/
 ┃ ┗ 📂 resources/
 ┃   ┗ 📄 application-test.properties
 ┣ 📂 docs/
 ┃ ┣ 📂 imagenes/
 ┃ ┣ 📂 pdf/
 ┃ ┗ 📂 uml/
 ┃   ┣ DiagramaContexto.png
 ┃   ┣ DiagramaComponentesGeneral.png
 ┃   ┣ DiagramaComponentesEspecificos.png
 ┃   ┣ DiagramaCasosUso.png
 ┃   ┣ DiagramaClases.png
 ┃   ┣ DiagramaBaseDeDatos.png
 ┃   ┗ diagramaDespliegue.png
 ┣ 📄 pom.xml
 ┣ 📄 docker-compose.yml
 ┣ 📄 Dockerfile
 ┣ 📄 mvnw / mvnw.cmd
 ┗ 📄 README.md
```

---

# 📡 API Endpoints

For detailed documentation refer to Swagger UI (running locally at `http://localhost:8080/swagger-ui.html`).

Below is a summary of the main REST endpoints exposed by `BookingController` (base path: `/api/v1/bookings`).

| Method | URI                        | Description                          | Request Body / Params |
| :----- | :------------------------- | :----------------------------------- | :-------------------- |
| `POST` | `/api/v1/bookings`         | Creates a new booking                 | `BookingRequestDTO` (JSON) |
| `GET`  | `/api/v1/bookings`         | Retrieves all bookings                | — |
| `GET`  | `/api/v1/bookings/{id}`    | Retrieves a booking by ID             | `id` (Path Variable) |
| `PUT`  | `/api/v1/bookings/{id}`    | Updates an existing booking          | `id` + `BookingRequestDTO` (JSON) |
| `DELETE` | `/api/v1/bookings/{id}`  | Cancels a booking by ID               | `id` (Path Variable) |


### 📟 HTTP Status Codes
Common status codes returned by the API.

| Code | Status | Description |
| :--- | :--- | :--- |
| `200` | **OK** | Request processed successfully. |
| `201` | **Created** | Booking created successfully. |
| `204` | **No Content** | Booking cancelled successfully. |
| `400` | **Bad Request** | Invalid data or missing parameters / Insufficient seats. |
| `404` | **Not Found** | Booking ID does not exist. |
| `500` | **Internal Server Error** | Unexpected error on server side.|

---

# Input and Output Data

Data information per functionality (simplified overview):

- **BookingRequestDTO (Input)**
  - `travelId` (String)
  - `passengerId` (String)
  - `numberOfSeats` (Integer)
  - Additional booking details

- **BookingResponseDTO (Output)**
  - `id` (String)
  - `travelId` (String)
  - `passengerId` (String)
  - `numberOfSeats` (Integer)
  - `status` (from `BookingStatus` enum: PENDING, CONFIRMED, CANCELLED)
  - `createdAt` / `updatedAt` (LocalDateTime)
  - Additional booking details

You can inspect `BookingRequestDTO` and `BookingResponseDTO` classes under `infrastructure/controllers/dto` for full details.

---

# 🔗 Connections with other Microservices

This module is part of the RideCi ecosystem and interacts with other services via RabbitMQ event-driven architecture:

1. **Travel Management Module (Nemesis)**: Consumes booking events (`BookingCreatedEvent`, `BookingCancelledEvent`) to update travel seat availability.
2. **Notifications Module**: Receives booking events to send alerts and confirmations to passengers about booking status changes.
3. **Payments Module**: May receive booking events to trigger payment processing workflows.
4. **Profiles / Passengers Module**: Provides passenger data associated with bookings.

**Event Publishing Configuration:**
- **RabbitMQ CloudAMQP**: `possum.lmq.cloudamqp.com:5671`
- **SSL enabled** with TLS v1.2+
- **Exchange**: `booking-events`
- **Events Published**: 
  - `BookingCreatedEvent`
  - `BookingUpdatedEvent`
  - `BookingCancelledEvent`

---

# Technologies

The following technologies were used to build and deploy this module:

### Backend & Core
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![MapStruct](https://img.shields.io/badge/MapStruct-1.5.5-orange?style=for-the-badge)
![Lombok](https://img.shields.io/badge/Lombok-BC4521?style=for-the-badge&logo=lombok&logoColor=white)

### Database
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)

### Messaging
![RabbitMQ](https://img.shields.io/badge/rabbitmq-%23FF6600.svg?&style=for-the-badge&logo=rabbitmq&logoColor=white)

### DevOps & Infrastructure
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Kubernetes](https://img.shields.io/badge/kubernetes-%23326ce5.svg?style=for-the-badge&logo=kubernetes&logoColor=white)
![Railway](https://img.shields.io/badge/Railway-131415?style=for-the-badge&logo=railway&logoColor=white)
![Vercel](https://img.shields.io/badge/vercel-%23000000.svg?style=for-the-badge&logo=vercel&logoColor=white)

### CI/CD & Quality Assurance
![GitHub Actions](https://img.shields.io/badge/github%20actions-%232671E5.svg?style=for-the-badge&logo=githubactions&logoColor=white)
![SonarQube](https://img.shields.io/badge/SonarQube-4E9BCD?style=for-the-badge&logo=sonarqube&logoColor=white)
![JaCoCo](https://img.shields.io/badge/JaCoCo-86%25_Coverage-brightgreen?style=for-the-badge)

### Documentation & Testing
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-C5D9C8?style=for-the-badge)

### Design
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)

### Communication & Project Management
![Jira](https://img.shields.io/badge/jira-%230A0FFF.svg?style=for-the-badge&logo=jira&logoColor=white)
![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)

---

**Key Technology Details:**
- **Java 17** (JDK-17)
- **Spring Boot 3.5.7** (web, data-mongodb, amqp)
- **MongoDB Atlas** cluster: `cluster0.huvfbuk.mongodb.net/MS_SEARCH_AND_BOOKING`
- **RabbitMQ CloudAMQP** (SSL): `possum.lmq.cloudamqp.com:5671`
- **MapStruct 1.5.5.Final** for DTO/Entity mapping
- **JaCoCo 0.8.12** for code coverage (86% achieved)
- **Lombok** for boilerplate reduction (@SuperBuilder for inheritance)

---

# 🌿 Branches Strategy & Structure

This module follows a strict branching strategy based on Gitflow to ensure ordered versioning, code quality and continuous integration.

| **Branch**                | **Purpose**                            | **Receive from**           | **Sent to**        | **Notes**                      |
| ----------------------- | ---------------------------------------- | ----------------------- | ------------------ | ------------------------------ |
| `main`                  | 🏁 Stable code for preproduction or Production | `release/*`, `hotfix/*` | 🚀 Production      | 🔐 Protected with PR and successful CI   |
| `develop`               | 🧪 Main developing branch             | `feature/*`             | `release/*`        | 🔄 Base for continuous deployment |
| `feature/*`             | ✨ New features or refactors to be implemented       | `develop`               | `develop`          | 🧹 Deleted after merge to develop      |
| `release/*`             | 📦 Release preparation & final polish      | `develop`               | `main` and `develop` | 🧪  Includes final QA. No new features added here     |
| `bugfix/*` or `hotfix/*` | 🛠️ Critical fixes for production         | `main`                  | `main` and `develop` | ⚡ Urgent patches. Highest priority             |

---

# 🏷️ Naming Conventions

## 🌿 Branch Naming

### ✨ Feature Branches
Used for new features or non-critical improvements.

**Format:**
```
feature/[shortDescription]
```

**Examples:**
- `feature/authentication-module`
- `feature/security-service`
- `feature/usecases`

**Rules:**
* 🧩 **Case:** strictly *kebab-case* (lowercase with hyphens)
* ✍️ **Descriptive:** Short and meaningful description
* Include Jira ticket code when available

---

### 📦 Release Branches
Used for preparing a new production release. Follows [Semantic Versioning](https://semver.org/).

**Format:**
```
release/v[major].[minor].[patch]
```

**Examples:**
- `release/v1.0.0`
- `release/v1.1.0-beta`

---

### 🚑 Hotfix Branches
Used for urgent fixes in the production environment.

**Format:**
```
hotfix/[shortDescription]
```

**Examples:**
- `hotfix/fix-token-expiration`
- `hotfix/security-patch`

---

## 📝 Commit Message Guidelines

We follow the **[Conventional Commits](https://www.conventionalcommits.org/)** specification.

### 🧱 Standard Format

```text
[jira-code] <type>: <short description>
```

**Examples:**
```
45-feat: add JWT token validation
46-fix: correct authentication role error
25-docs: update README with new routes
27-refactor: optimize security service
29-test: add tests for AuthService
30-chore: update Maven dependencies
```

### 🧱 Commit Types

| **Type**   | **Description**                      | **Example**                                     |
| ----------- | ------------------------------------ | ----------------------------------------------- |
| `feat`      | New feature                  | `22-feat: implement JWT authentication`    |
| `fix`       | Bug fix                | `24-fix: resolve login endpoint error` |
| `docs`      | Documentation changes             | `25-docs: update README with new routes`   |
| `refactor`  | Refactoring without functional change | `27-refactor: optimize security service`  |
| `test`      | Unit or integration tests   | `29-test: add tests for AuthService`       |
| `chore`     | Maintenance or configuration        | `30-chore: update Maven dependencies`    |

**Rules:**
* One commit = one complete action
* Maximum **72 characters** per line
* Use imperative mood ("add", "fix", etc.)
* Clear description of what and where
* Small and frequent commits

---

# 📐 System Architecture & Design

This section provides a visual representation of the module's architecture illustrating the base diagrams to show the application structure and components flow.

## 🧩 Context Diagram

This diagram shows how the Search and Booking module fits within the RideCi ecosystem and its interactions with other microservices.

---

![Context Diagram](./docs/uml/DiagramaContexto.png)

---

## 🧩 Deployment Diagram

This diagram illustrates the cloud deployment architecture and workflow of the search and booking module.

The architecture includes:

- **Client (Front-End Web App)**: React + TypeScript application communicating via HTTPS and WebSockets
- **Search and Book Backend**: Spring Boot microservice with JaCoCo, SonarQube, and Docker
- **MongoDB Database**: Atlas cluster storing bookings and travel data
- **External Microservices**:
  - Travel Management (Nemesis)
  - Notifications (Email/App)
  - Payments (Registration)

![Deployment Diagram](./docs/uml/diagramaDespliegue.png)

---

## 🧩 General Components Diagram

This diagram describes the main components, technologies and communication flow of the RideCI system, oriented to travel search and booking.

The architecture is composed of:

- **Frontend (RideCI Front)**: TypeScript, React, Figma (UI/UX), deployed on Vercel
- **API Gateway**: Centralizes requests from frontend and distributes to microservices
- **Microservice Search and Booking**: Manages travel searches and bookings
- **Database Travel DB**: MongoDB storing travel and booking data

![General Components Diagram](./docs/uml/DiagramaComponentesGeneral.png)

---

## 🧩 Specific Components Diagram

This diagram visualizes the internal architecture of the **Search and Booking** microservice, following **Clean Architecture** and **Hexagonal Architecture (Ports & Adapters)** principles.

It includes:

### Controllers
Point of entry for requests from the API Gateway:
- **SearchController**: Handles search and travel availability requests
- **BookingController**: Manages creation, cancellation, query and update of bookings

### Use Cases (Business Logic)
- **Search**: GetTravelUseCase, SearchAvailableTravelsUseCase
- **Bookings**: CreateBookingUseCase, CancelBookingUseCase, GetBookingUseCase, PutBookingUseCase, NotifyBookingStatusUseCase
- **Confirmation & Payment**: ConfirmBookingUseCase, ConfirmPaymentUseCase, ValidateBookingAvailabilityUseCase

### Ports (Interfaces)
Interfaces allowing decoupling between business logic and external services:
- SearchAvailableTravelsPort
- BookingRepositoryPort
- ValidateBookingAvailabilityPort
- ConfirmBookingPort
- ConfirmPaymentPort

### Adapters (Implementations)
Concrete implementations of ports, responsible for interacting with external systems:
- Search Adapters: SearchAdapter, SearchAvailableTravelsAdapter, MapperSearchAdapter
- Booking Adapters: BookingAdapter, MapperBookingAdapter
- Confirmation & Payment Adapters: ConfirmBookingAdapter, ConfirmPaymentAdapter, ValidateBookingAvailabilityAdapter

### Repositories
Data access and persistence handlers:
- BookingRepository
- BookingRepositoryAdapter

### External Integrations
Connections with other specialized microservices:
- **TravelManagement (Nemesis)**: Travel queries and availability updates
- **Notifications**: Sending notifications about booking status
- **Payments**: Payment management and confirmation

All external services use: Spring Boot, Docker, SonarQube, JaCoCo

![Specific Components Diagram](./docs/uml/DiagramaComponentesEspecificos.png)

---

## 🧩 Use Cases Diagram

This diagram presents the main functionalities defined by each actor, facilitating better understanding when implementing the module's multiple functions and identifying each actor's roles.

![Use Cases Diagram](./docs/uml/DiagramaCasosUso.png)

---

## 🧩 Class Diagram

Based on the Specific Components diagram, this class diagram shows the domain models, their relationships, and the Observer design pattern implementation for notifying passengers about booking changes.

![Class Diagram](./docs/uml/DiagramaClases.png)

---

## 🧩 Database Diagram

This diagram represents how data is stored in MongoDB, showing the document structure and relationships between collections.

Key collections:
- **Bookings**: Main collection storing booking documents with passenger, travel, and seat information
- Embedded documents for optimization
- Referenced documents for normalization

![Database Diagram](./docs/uml/DiagramaBaseDeDatos.png)

---

# 🚀 Getting Started

This section guides you through setting up the project locally. This project requires **Java 17**. If you have a different version, you can change it or we recommend using **Docker** to ensure compatibility before compiling.

## Prerequisites

- **Java 17** (JDK-17)
- **Maven 3.8+**
- **Docker** (optional but recommended)
- **MongoDB** account (MongoDB Atlas) or local instance
- **RabbitMQ** account (CloudAMQP) or local instance

---

## Clone & Open Repository

```bash
git clone https://github.com/RIDECI/POSEIDON_SEARCH_AND_BOOKING.git
```

```bash
cd POSEIDON_SEARCH_AND_BOOKING
```

You can open it in your favorite IDE (IntelliJ IDEA, Eclipse, VS Code).

---

## Environment Configuration

Create a `.env` file or configure `application.properties` with the following variables:

```properties
# MongoDB Configuration
spring.data.mongodb.uri=mongodb+srv://<username>:<password>@cluster0.huvfbuk.mongodb.net/MS_SEARCH_AND_BOOKING?retryWrites=true&w=majority

# RabbitMQ Configuration
spring.rabbitmq.host=possum.lmq.cloudamqp.com
spring.rabbitmq.port=5671
spring.rabbitmq.username=<your-username>
spring.rabbitmq.password=<your-password>
spring.rabbitmq.virtual-host=<your-vhost>
spring.rabbitmq.ssl.enabled=true
spring.rabbitmq.ssl.algorithm=TLSv1.2

# Server Configuration
server.port=8080
```

**Note**: Replace `<username>`, `<password>`, `<your-username>`, `<your-password>`, and `<your-vhost>` with your actual credentials.

---

## Dockerize the Project (Recommended)

Dockerizing before compiling the project avoids configuration issues and ensures environment consistency.

```bash
docker compose up -d
```

This will start MongoDB and RabbitMQ containers locally if configured in `docker-compose.yml`.

---

## Install Dependencies & Compile Project

Download dependencies and compile the source code.

```bash
mvn clean install
```

```bash
mvn clean compile
```

---

## Run the Project

Start the Spring Boot server:

```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

---

## Access Swagger Documentation

Once the application is running, access the interactive API documentation at:

```
http://localhost:8080/swagger-ui.html
```

---

# 🧪 Testing

Testing is an essential part of the project functionality. This section shows the code coverage and code quality analyzed with tools like JaCoCo and SonarQube.

## Test Suite Overview

The project includes **75 comprehensive tests** across 10 test classes, achieving **86% code coverage**:

### Unit Tests

#### Application Layer (35 tests)
- **BookingServiceTest.java** - 14 tests
  - Tests for business logic, booking creation, updates, cancellation
  - Seat availability validation
  - Exception handling

- **RabbitMQEventPublisherTest.java** - 2 tests
  - Event publishing to RabbitMQ
  - Message conversion and routing

- **MappersIntegrationTest.java** - 6 tests
  - Integration tests with @SpringBootTest
  - BookingMapper and BookingMapperInitial functionality
  - Bidirectional mapping, null handling, status mapping

- **DTOsTest.java** - 5 tests
  - Request DTO validation
  - Response DTO construction

- **DTOsInfrastructureTest.java** - 8 tests
  - Infrastructure layer DTOs
  - Data transformation accuracy

#### Domain Layer (12 tests)
- **BookingDomainTest.java** - 6 tests
  - Domain model behavior
  - Business rules validation
  - Booking state transitions

- **BookingDocumentTest.java** - 6 tests
  - MongoDB document entity
  - Builder pattern functionality
  - Equals/hashCode/toString methods
  - Entity state management

#### Infrastructure Layer (28 tests)
- **BookingControllerTest.java** - 10 tests (MockMvc)
  - REST endpoint testing
  - Request/response validation
  - HTTP status codes
  - Error handling

- **BookingRepositoryAdapterTest.java** - 14 tests
  - Repository adapter behavior
  - Database interaction mocking
  - CRUD operations
  - Query methods

- **ExceptionsTest.java** - 4 tests
  - Custom exception handling
  - BookingNotFoundException
  - InsufficientSeatsException

---

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Tests with Coverage Report
```bash
mvn clean test jacoco:report
```

### View Coverage Report
After running tests, open the JaCoCo HTML report:
```
target/site/jacoco/index.html
```

---

## 📊 Code Coverage (JaCoCo)

Current coverage: **86%** (exceeding the 80% target)

### Coverage Breakdown by Package

| Package | Coverage |
|---------|----------|
| `edu.dosw.rideci.infrastructure.adapters` | **100%** |
| `edu.dosw.rideci.application.service` | **100%** |
| `edu.dosw.rideci.infrastructure.controllers` | **100%** |
| `edu.dosw.rideci.exceptions` | **100%** |
| `edu.dosw.rideci.infrastructure.persistence.entity` | **76%** |
| `edu.dosw.rideci.application.event` | **70%** |

### Overall Metrics
- **Instructions**: 981 of 1,129 covered (86%)
- **Lines**: 156 of 158 covered (98%)
- **Methods**: 74 of 87 covered (85%)
- **Classes**: 11 of 11 covered (100%)

### JaCoCo Configuration

The project is configured to exclude auto-generated code from coverage metrics:

```xml
<execution>
    <id>jacoco-check</id>
    <goals>
        <goal>check</goal>
    </goals>
    <configuration>
        <rules>
            <rule>
                <element>BUNDLE</element>
                <limits>
                    <limit>
                        <counter>INSTRUCTION</counter>
                        <value>COVEREDRATIO</value>
                        <minimum>0.80</minimum>
                    </limit>
                </limits>
            </rule>
        </rules>
        <excludes>
            <exclude>**/mappers/**/*Impl.class</exclude>
        </excludes>
    </configuration>
</execution>
```

**Note**: MapStruct-generated implementations (`*Impl.class`) are excluded from coverage as they are auto-generated code.

---

## 🔍 Static Analysis (SonarQube)

The project is configured for continuous quality analysis with SonarQube, monitoring:

- Code smells
- Security vulnerabilities
- Technical debt
- Code duplication
- Complexity metrics

SonarQube analysis runs automatically in the CI/CD pipeline via GitHub Actions.

---

## Test Best Practices

The test suite follows these best practices:

✅ **Arrange-Act-Assert (AAA)** pattern  
✅ **Meaningful test names** describing the scenario  
✅ **Mocking external dependencies** (MongoDB, RabbitMQ)  
✅ **Test isolation** - each test is independent  
✅ **Edge case coverage** - null values, empty lists, boundary conditions  
✅ **Integration tests** for critical flows with @SpringBootTest  
✅ **Exception testing** - verify error handling behavior  
✅ **Builder pattern usage** - clean test data creation  

---

## Continuous Integration

Tests run automatically on every push and pull request via GitHub Actions, ensuring:

- All tests pass before merge
- Coverage remains above 80%
- No new bugs or vulnerabilities introduced
- Code quality standards maintained

---

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🤝 Contributing

Contributions are welcome! Please follow the branching strategy and commit conventions outlined in this README.

1. Fork the repository
2. Create your feature branch (`feature/amazing-feature`)
3. Commit your changes following conventional commits
4. Push to the branch
5. Open a Pull Request

---

## 📧 Contact

For questions or support, please contact the development team via Slack or Jira.

---

**Built with ❤️ by the RideCi Team**
