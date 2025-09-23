FROM eclipse-temurin:17-jre-jammy

# Add non root user
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*
RUN groupadd -r spring && useradd -r -g spring spring

WORKDIR /app

COPY build/libs/core-0.0.1-SNAPSHOT.jar core.jar

# Set ownership to non-root user
RUN chown -R spring:spring /app

USER spring

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=5 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "core.jar"]