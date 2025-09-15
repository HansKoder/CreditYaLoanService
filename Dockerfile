# ---------- Stage 1: Build ----------
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace

# Copy graddle wrapper and setup root
COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY build.gradle .
COPY main.gradle .
COPY gradle.properties .
RUN chmod +x ./gradlew

# Copy each layer clean, since gradle needs to see them
COPY applications ./applications
COPY domain ./domain
COPY infrastructure ./infrastructure

# Compile and generate Jar
RUN ./gradlew :app-service:clean :app-service:build -x test


# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app

# Create user no-root
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

# Copy jar from stage (builder)
COPY --from=builder /workspace/applications/app-service/build/libs/*.jar app.jar

# JVM
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

EXPOSE 9082
USER appuser

ENTRYPOINT ["java","-jar","/app/app.jar"]