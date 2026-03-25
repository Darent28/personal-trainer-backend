# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run the application
./mvnw spring-boot:run

# Build
./mvnw clean package

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=ClassName

# Run a single test method
./mvnw test -Dtest=ClassName#methodName
```

## Environment Setup

The app requires a PostgreSQL connection. Create a `.env` file in the project root with:

```
SPRING_DATASOURCE_USERNAME=<username>
SPRING_DATASOURCE_PASSWORD=<password>
```

The datasource URL points to a Neon Cloud PostgreSQL instance (configured in `src/main/resources/application.properties`). A commented-out local PostgreSQL URL (port 5433) is also present for local development. Server runs on port 8080.

## Architecture

**Stack:** Java 21, Spring Boot 4.x, Spring Data JPA, PostgreSQL, Maven, Lombok

**Layered architecture:** `Controller → Service → Repository → Entity (JPA)`

All source code is under `src/main/java/com/pt/personal_trainer/`.

**Key layers:**
- `controller/` — REST controllers (`UserController`, `InfoController`)
- `service/` — Business logic (`UserService`, `InfoUserService`)
- `repository/` — Spring Data JPA repositories
- `entity/` — JPA entities (`User`, `InfoUser`, `GoalType`, `LevelActivityType`)
- `domain/input/` — Request DTOs with JSR-380 validation constraints
- `domain/dto/` — Response DTOs as immutable Java records with static `fromEntity()` factory methods
- `exception/` — Custom exception hierarchy (`NotFoundException`, `ProcessServiceException`, `ServerErrorException`) and a global `@RestControllerAdvice` handler that returns `ProblemDetail` responses
- `config/` — `PasswordEncoderConfig` (BCrypt bean)

**Patterns in use:**
- Constructor injection (no `@Autowired`)
- `@Transactional` on write operations in service layer
- Soft delete: `updateStatusById()` in `UserRepository` sets a status flag rather than deleting rows
- Password encoding via BCrypt at the service layer before persistence
- Custom `@Query` annotations in repositories for non-destructive update operations

## Core Feature: Macro Calculation

`InfoUserService` implements the **Harris-Benedict BMR formula** to calculate daily calorie and macronutrient targets. It uses `LevelActivityType.activityFactor` (fetched by ID) to scale BMR based on the user's activity level. The result drives the diet plan stored in `InfoUser`.

## API Endpoints

**User management** (`/api/user`):
- `POST /api/user/create-user` — Create user
- `GET /api/user/get-users` — Get all users
- `GET /api/user/get-user/{id}` — Get user by ID
- `PUT /api/user/user/{id}` — Update user
- `PUT /api/user/delete-user/{id}` — Soft delete (sets status flag)

**User info/diet** (`/api/info`):
- `POST /api/info/create` — Create user info and calculate macros

## Known Issues

- `InfoUser.goal` is typed as `Integer` but annotated with `@ManyToOne` — should reference the `GoalType` entity
- `UserRepository.updateStatusById()` references a `status` column not present on the `User` entity
- Column name typo in DB: `_wheight` (should be `weight`)
- `GoalTypeRepository` extends `JpaRepository` but has no custom methods and is not wired into any service
- `ddl-auto=update` is set — schema changes apply automatically on startup
