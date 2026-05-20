# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Run the application
./mvnw spring-boot:run

# Build JAR (skipping tests, e.g. for Docker)
./mvnw clean package -DskipTests

# Run all tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=ClassName

# Run a single test method
./mvnw test -Dtest=ClassName#methodName
```

## Environment Setup

Create a `.env` file in the project root. Required variables:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://...
SPRING_DATASOURCE_USERNAME=...
SPRING_DATASOURCE_PASSWORD=...
JWT_SECRET=<hex string>
RESEND_API_KEY=<Resend email API key>
RESEND_FROM=<sender email>
APP_BASE_URL=http://localhost:8080
CONFIRMATION_TOKEN_EXPIRY_HOURS=24
PASSWORD_RESET_TOKEN_EXPIRY_HOURS=1
```

`application.properties` imports this file via `spring.config.import=optional:dotenv:`. A commented-out local PostgreSQL URL (port 5433) is present for local development. `ddl-auto=update` applies schema changes automatically on startup. Tests use H2 in-memory — no local DB needed to run tests.

Resend is used instead of SMTP because Railway blocks outbound SMTP connections. Do not replace it with JavaMailSender.

## Architecture

**Stack:** Java 21, Spring Boot 4.x, Spring Security (JWT), Spring Data JPA, PostgreSQL, Maven, Lombok, JJWT 0.12.6, Resend SDK

**Layered architecture:** `Controller → Service → Repository → Entity (JPA)`

All source code is under `src/main/java/com/pt/personal_trainer/`.

**Packages:**
- `controller/` — REST controllers: `AuthController`, `UserController`, `InfoController`, `LookupController`
- `service/` — Business logic: `AuthService`, `UserService`, `InfoUserService`, `EmailConfirmationService`, `PasswordResetService`, `EmailService`
- `repository/` — Spring Data JPA repositories with custom `@Query` methods
- `entity/` — JPA entities: `User`, `InfoUser`, `DailyPlans`, `GoalType`, `LevelActivityType`, `GoalMacroConfig`, `EmailConfirmationToken`, `PasswordResetToken`
- `domain/input/` — Request DTOs with JSR-380 validation constraints
- `domain/dto/` — Response DTOs as immutable Java records with static `fromEntity()` factory methods
- `exception/` — Custom hierarchy (`NotFoundException` → 404, `ProcessServiceException` → 400, `ServerErrorException` → 500) with a global `@RestControllerAdvice` returning `ProblemDetail` responses (RFC 9457)
- `auth/` — `JwtUtil`, `JwtAuthFilter` (OncePerRequestFilter), `UserDetailsServiceImpl`, `SecurityConfig`
- `config/` — `JwtProperties`, `AppProperties` (property bindings)

**Patterns in use:**
- Constructor injection (no `@Autowired` field injection)
- `@Transactional` on write operations in service layer
- Soft delete: `UserRepository.updateStatusById()` sets a status flag rather than deleting rows
- Password encoding via BCrypt at the service layer before persistence
- Custom `@Query` annotations in repositories for non-destructive update operations
- Exception-driven control flow: services throw custom exceptions caught by `@RestControllerAdvice`

## Security

JWT-based stateless authentication. `SecurityConfig` permits `/api/auth/**` and `/api/cron/**` and denies everything else without a valid Bearer token. `JwtAuthFilter` validates tokens and populates the `SecurityContext`. Tokens embed `userId` and `username` as claims alongside the email subject.

CORS allowed origins are hardcoded in `SecurityConfig` (production Railway URL, `personal-trainer.one`, `localhost:5173`). CSRF is disabled. Sessions are stateless.

Email verification is required before login — `AuthService.login()` checks `email_verified` before issuing a JWT.

## Core Feature: Macro Calculation

`InfoUserService.calculateMacros()` implements:
1. **BMR** via Harris-Benedict formula (gender-specific: male `+5`, female `-161`)
2. **TDEE** = BMR × activity factor (fetched from `LevelActivityType` via `LevelActivityTypeRepository.findFactorById()`)
3. **Adjustment factor `t`** based on body fat percentage: `t = min(fatPercentage / 30, 1.0)` — defaults to 20% (male) or 30% (female) if not provided; calorie offset scales as `t²` (non-linear), protein/fat scale linearly
4. **Goal config ranges** fetched from `GoalMacroConfig` by goal type ID (Cut/Bulk/Recomposition)
5. **Final macros:** calories = TDEE + offset; proteins and fats derived from per-kg ranges × weight; carbs fill remaining calories

Result is persisted as a `DailyPlans` record linked to `InfoUser`.

## API Endpoints

**Auth** (`/api/auth`, public): `POST /register`, `POST /login`, `POST /forgot-password`, `GET /reset-password?token=`, `PUT /reset-password`, `GET /confirm-email?token=`

**Users** (`/api/users`, JWT required): `GET /`, `GET /{id}`, `PUT /{id}`, `DELETE /{id}`

**Info/Diet** (`/api/info`, JWT required): `POST /`, `GET /{id}`, `GET /user/{userId}`, `GET /{id}/daily-plan`

**Lookup** (`/api/lookup`, JWT required): `GET /goal-types`, `GET /activity-levels`

## Testing

Tests use `@WebMvcTest` with `@AutoConfigureMockMvc(addFilters = false)` (disables JWT security filters) and `@MockitoBean` for service dependencies. When constructing `ObjectMapper` in tests, always call `.findAndRegisterModules().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)` to handle `LocalDate`/`Instant` serialization correctly — omitting this causes silent test failures with date fields.

## Docker

Two-stage Dockerfile: build with `eclipse-temurin:21-jdk` (`./mvnw clean package -DskipTests`), run with `eclipse-temurin:21-jre-alpine`. All env vars must be injected at runtime (Railway injects them automatically from project settings).

## Known Issues

- `InfoUser.goal` is typed as `Integer` but should be a `@ManyToOne` reference to `GoalType`
- `UserRepository.updateStatusById()` references a `status` column not present on `User` entity
- Column name typo in DB: `_weight` (should be `weight`) in `users_info` table — mapped via `@Column(name = "_weight")` on the entity
- `GoalMacroConfig.fatPerKgMin/Max` are fetched but the fat interpolation uses a hardcoded `0.5` factor instead of the goal-specific `macroFactor`
