# Loan Service

## 📌 Overview
The **Loan Service** manages the lifecycle of loans for each customer.  
It is responsible for:
- Create loan applications (Customer side)
- Retrieve loans (Advisor side)

This service is built following **Clean Architecture** principles to ensure testability, maintainability, and scalability.

---

## 🏛️ Architecture & Patterns

This service applies **Event Sourcing** and **CQRS** to separate responsibilities and provide traceability:

- **Event Sourcing**  
  Every important change (e.g. *LoanApplicationSubmitted*, *LoanApproved*) is stored as an **event** rather than only keeping the latest state.  
  This allows:
    - A full history of loan changes.
    - Replayability of events to rebuild state.
    - Auditability.

- **CQRS (Command Query Responsibility Segregation)**  
  Separation of responsibilities between:
    - **Write model (Loan Aggregate)** → Validates commands, applies business rules, and persists events in **PostgreSQL** (Event Store).
    - **Read model (LoanRead)** → Stores projections in **MongoDB** for fast queries, filtering, and pagination.

👉 This way:
- **Loan - Write** → reliable and consistent business decisions.
- **Loan - Read** → optimized, flexible views for queries.

> 📌 A diagram will be included in the future to illustrate the flow.

---

## ⚙️ Tech Stack

| Layer              | Technology                                                                                                                             |
|--------------------|----------------------------------------------------------------------------------------------------------------------------------------|
| **Language**       | Java 21                                                                                                                                |
| **Framework**      | Spring Boot 3.5 + Spring WebFlux (Reactive stack)                                                                                      |
| **Security**       | Spring Security (Reactive), JWT, BCrypt                                                                                                |
| **Persistence**    | PostgreSQL (Event Store - Write), MongoDB (Projections - Read)                                                                         |
| **Build Tool**     | Gradle                                                                                                                                 |
| **Containers**     | Docker                                                                                                                                 |
| **TestContainers** | Integration Test Mock Postgres - Mongodb etc                                                                                           |
| **Architecture**   | Clean Architecture (Bancolombia’s Open Source Tool) [More Info](https://bancolombia.github.io/scaffold-clean-architecture/docs/intro/) |

---

## 📂 Architecture Structure
- **Domain Layer**: Business rules (entities, value objects, domain events, contracts).
- **Application Layer**: Use cases (loan application, loan query, projections).
- **Infrastructure Layer**:
    - **Driven Adapters**:
        - R2dbc-postgresql (Postgres R2DBC)
        - Mongo (Mongo Reactive)
        - Security (JWT)
        - Event Dispatcher (publishes domain events to projections)
        - Rest Consumer (in order to check customer exist make request to customer-service)
    - **Entry Points**:
        - Reactive Web (WebFlux handlers / routers) 

---

## 🚀 Getting Started

### Prerequisites
- JDK 21+
- Docker 
- Generate a Token using auth-service, read its readme for more details and set token (Bearer) in each request.

## Testing

### Test
```bash
./gradlew test
```


### Integration Test
```bash
./gradlew integrationTest
```