# =================
# Stage 1: The "Build" Stage
# Use a Gradle image to build the application JAR
# =================
FROM gradle:jdk17 AS builder

WORKDIR /app

# Copy only the necessary build files to leverage layer caching
COPY build.gradle gradlew settings.gradle ./
COPY gradle/ ./gradle/

# Copy the application source code
COPY src/ ./src/

# Run the Gradle build command to produce the JAR file
RUN ./gradlew build --no-daemon

# =================
# Stage 2: The "Runtime" Stage
# Use your preferred lightweight image for the final container
# =================
FROM eclipse-temurin:17-jre-jammy

# Add non-root user and install curl for the healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app

# --- THIS IS THE KEY CHANGE ---
# Copy the JAR file FROM THE "builder" STAGE
COPY --from=builder /app/build/libs/core-0.0.1-SNAPSHOT.jar core.jar

# Set ownership to non-root user
RUN chown -R spring:spring /app

# Switch to the non-root user
USER spring

EXPOSE 8080

# Your healthcheck remains the same
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=5 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Your entrypoint remains the same
ENTRYPOINT ["java", "-jar", "core.jar"]
