# ===== Stage 1: Build the app with Gradle =====
FROM gradle:8.7-jdk17 AS build
WORKDIR /app

# Copy Gradle files first (better layer caching - deps only re-download if these change)
COPY build.gradle settings.gradle ./
COPY src ./src

RUN gradle clean build -x test --no-daemon

# ===== Stage 2: Run the app with a lightweight JRE =====
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]