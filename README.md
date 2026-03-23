# AL Carbon Calculator

## Project Overview

This repository contains the backend implementation for the Carbon Calculator technical challenge. The application is a RESTful API built with Java and Spring Boot, designed to calculate a user's carbon footprint based on their consumption of electricity, transportation, and solid waste.

The system uses pre-defined emission factors stored in a MongoDB database to convert raw consumption data into a consolidated carbon emission result.

## Architecture & Technical Decisions

To ensure a clean, maintainable, and testable codebase, the application was designed following the **Separation of Concerns (Clean Architecture)** principles:

* **Controllers (`br.com.actionlabs.carboncalc.rest`):** Act strictly as the entry point for HTTP requests. They delegate business logic to the service layer, keeping the presentation layer thin.
* **Services (`br.com.actionlabs.carboncalc.service`):** The core of the application. Contains all the business rules, including the specific logic for separating recyclable and non-recyclable waste before applying emission factors.
* **Repositories & Models:** Handled by Spring Data MongoDB, providing a seamless abstraction for database operations without boilerplate queries.
* **Global Exception Handling:** A `@RestControllerAdvice` was implemented to gracefully catch exceptions (e.g., "Resource Not Found") and return standardized JSON error responses with appropriate HTTP status codes (like `400 Bad Request`), preventing stack traces from leaking to the client.

## Technologies Used
* **Java 17**
* **Spring Boot 3.3.4** (Web, Data MongoDB)
* **MongoDB** (via Docker)
* **Lombok** (to reduce boilerplate code)

## How to Run the Application

### Prerequisites
* Docker and Docker Compose installed.
* Java 17 JDK installed.
* A preferred IDE (IntelliJ IDEA, Eclipse, etc.).

### Step 1: Start the Database
Open a terminal in the root directory of the project and run the following command to start the MongoDB container. The `init-mongo.js` script will automatically populate the database with the default emission factors:

```
docker compose up -d
```

### Step 2: Run the Spring Boot application:

```
./gradlew bootRun
```

### Step 3: Access Swagger UI
Access the API documentation and test the endpoints via Swagger UI:
👉 http://localhost:8085/swagger-ui.html