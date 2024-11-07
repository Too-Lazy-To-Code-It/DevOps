FROM openjdk:17-jdk-slim

WORKDIR /app

# Ensure that the JAR file is in the target directory after the Maven build
COPY target/gestion-station-ski.jar app.jar

EXPOSE 8089

ENTRYPOINT ["java", "-jar", "app.jar"]
