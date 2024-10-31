# Use an official OpenJDK image
FROM openjdk:8-jdk-alpine

# Set the working directory
WORKDIR /app

# Expose the application port
EXPOSE 8089

# Install wget to download the artifact
RUN apk add --no-cache wget

# Download the artifact into the working directory
RUN wget --user=admin --password=nexus -O gestion-station-ski-1.0.jar http://10.0.2.15:8081/repository/maven-releases/tn/esprit/spring/gestion-station-ski/1.0/gestion-station-ski-1.0.jar

# Verify that the file was downloaded
RUN ls -l gestion-station-ski-1.0.jar

# Run the downloaded JAR file
ENTRYPOINT ["java", "-jar", "gestion-station-ski-1.0.jar"]
