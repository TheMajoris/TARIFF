# Stage 1: Build the Spring Boot JAR
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

# Copy Gradle/Maven wrapper and build files
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
# Copy source code
COPY src ./src

# Make Gradle wrapper executable
RUN chmod +x ./gradlew

# Build the JAR and skip tests
RUN ./gradlew clean bootJar -x test --no-daemon

# Stage 2: Run the JAR
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Add non-root user
RUN groupadd -r spring && useradd -r -g spring spring

# Copy JAR from the build stage
COPY --from=build /app/build/libs/*.jar core.jar

# Set ownership to non-root user
RUN chown -R spring:spring /app
USER spring

EXPOSE 8080

# Healthcheck
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=5 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "core.jar"]
