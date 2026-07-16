# ===== Stage 1: Build the app using the project's own Gradle wrapper =====
# Using a plain JDK image (not a "gradle:X" image) means the build uses
# whatever Gradle version is pinned in gradle-wrapper.properties - avoiding
# "Spring Boot plugin requires Gradle X.x" mismatches entirely.
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# Make the wrapper script executable (permissions can get lost via git/CI)
RUN chmod +x gradlew

# Download dependencies first for better Docker layer caching
RUN ./gradlew dependencies --no-daemon || true

COPY src ./src

RUN ./gradlew clean build -x test --no-daemon

# ===== Stage 2: Run the app with a lightweight JRE =====
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]