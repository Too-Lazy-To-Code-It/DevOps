FROM maven:3.8.5-openjdk-8-slim AS builder

# Set the working directory
WORKDIR /app

# Copy your pom.xml and source code to the container
COPY pom.xml .
COPY src ./src

# Build the JAR file
RUN mvn clean package

# Second stage: use a smaller image for running the application
FROM openjdk:8-jdk-alpine
EXPOSE 8089

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/gestion-station-ski-1.0.jar /app/gestion-station-ski-1.0.jar

# Run the downloaded JAR file from the /app directory
ENTRYPOINT ["java", "-jar", "/app/gestion-station-ski-1.0.jar"]
