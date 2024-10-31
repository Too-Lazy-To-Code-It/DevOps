FROM openjdk:8-jdk-alpine

# Expose the application port
EXPOSE 8089

# Create a directory for the application
RUN mkdir -p /app

# Copy the pom.xml file and the src directory into the container
COPY pom.xml /app/
COPY src /app/src/

# Install wget (if not already included in the image)
RUN apk add --no-cache wget

# Download the JAR file using wget
RUN wget --user=admin --password=nexus -O /app/gestion-station-ski-1.0.jar http://10.0.2.15:8081/repository/maven-releases/tn/esprit/spring/gestion-station-ski/1.0/gestion-station-ski-1.0.jar

# Verify that the JAR file has been downloaded successfully
RUN ls -l /app/gestion-station-ski-1.0.jar

# Set the working directory to /app
WORKDIR /app

# Define the entry point for the container
ENTRYPOINT ["java", "-jar", "gestion-station-ski-1.0.jar"]
