# Use a base image with Maven and OpenJDK
FROM maven:3.8.6-openjdk-17-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY pom.xml .
COPY src ./src

# Build the Spring Boot application with Maven
RUN mvn clean install -DskipTests

# Use a smaller image to run the application
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build stage to the final image
COPY --from=build /app/target/gestion-station-ski.jar app.jar

# Expose the application port
EXPOSE 8089

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
