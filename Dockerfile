# Use an OpenJDK base image with Maven
FROM maven:3.8.6-openjdk-17-slim as build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project’s pom.xml and the source code
COPY pom.xml .
COPY src ./src

# Build the project using Maven
RUN mvn clean package -DskipTests

# Create a new image based on OpenJDK for running the application
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/gestion-station-ski.jar app.jar

# Expose the application port
EXPOSE 8089

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
