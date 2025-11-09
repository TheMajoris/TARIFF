# Stage 1: Build the Spring Boot JAR
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

# Copy Gradle wrapper and build files first (for better caching)
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .

# Make Gradle wrapper executable
RUN chmod +x ./gradlew

# Download dependencies first (this layer will be cached if dependencies don't change)
RUN ./gradlew dependencies --no-daemon || true

# Copy source code last (changes most frequently)
COPY src ./src

# Build the JAR and skip tests
RUN ./gradlew clean bootJar -x test --no-daemon

# Stage 2: Run the JAR
FROM eclipse-temurin:17-jre-jammy

# Set timezone to Singapore
ENV TZ=Asia/Singapore
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

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
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=10 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "core.jar"]