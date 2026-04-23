# --- Stage 1: Build ---
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# --- Stage 2: Runtime ---
FROM openjdk:17-slim
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/sanskriti-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
