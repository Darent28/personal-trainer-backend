# ── Build stage ──────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven wrapper and project descriptor
COPY .mvn .mvn
COPY mvnw pom.xml settings.xml ./
RUN chmod +x mvnw

# Cache dependencies + annotation processor paths (Lombok requires both)
RUN ./mvnw -B dependency:go-offline dependency:resolve-plugins

# Build the jar (skip tests in image build)
COPY src ./src
RUN touch .env && ./mvnw clean package -DskipTests -B

# ── Runtime stage ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE ${SERVER_PORT:-8080}

ENTRYPOINT ["java", "-jar", "app.jar"]
