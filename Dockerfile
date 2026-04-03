# ── Build stage ──────────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Cache dependencies + annotation processor paths (Lombok requires both)
COPY pom.xml .
RUN mvn -B dependency:go-offline dependency:resolve-plugins && mvn -B dependency:resolve -Dclassifier=sources

# Build the jar (skip tests in image build)
COPY src ./src
RUN touch .env && mvn clean package -DskipTests -B

# ── Runtime stage ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE ${SERVER_PORT:-8080}

ENTRYPOINT ["java", "-jar", "app.jar"]
