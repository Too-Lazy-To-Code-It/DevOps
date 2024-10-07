# Use an official Maven image to build the application
FROM maven:3.6.3-openjdk-17 AS build

# Use an official OpenJDK image to run the application
FROM openjdk:17-jdk-alpine

# Copy the built application from the Maven image
COPY --from=build /app/target/my-app.jar /app/my-app.jar

# Set the working directory in the container
WORKDIR /app

# Run the application
CMD ["java", "-jar", "my-app.jar"]
