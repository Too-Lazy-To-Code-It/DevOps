FROM openjdk:8-jdk-alpine
EXPOSE 8082

# Install wget to download the artifact
RUN apk add --no-cache wget

# Create a directory for the JAR file
RUN mkdir -p /app

# Download the artifact into the /app directory
RUN wget --user=admin --password=nexus -O /app/gestion-station-ski-1.0.jar http://10.0.2.15:8081/repository/maven-releases/tn/esprit/spring/gestion-station-ski/1.0/gestion-station-ski-1.0.jar

# Verify that the file was downloaded
RUN ls -l /app/gestion-station-ski-1.0.jar

# Run the downloaded JAR file from the /app directory
ENTRYPOINT ["java", "-jar", "/app/gestion-station-ski-1.0.jar"]