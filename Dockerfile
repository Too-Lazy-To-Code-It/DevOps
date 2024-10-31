FROM openjdk:8-jdk-alpine
EXPOSE 8089

# Install necessary packages
RUN apk add --no-cache wget

# Create application directory
WORKDIR /app

# Copy the Maven configuration and source code
COPY pom.xml ./
COPY src ./src/

# Download the JAR file
RUN wget --user=admin --password=nexus -O gestion-station-ski-1.0.jar \
    http://10.0.2.15:8081/repository/maven-releases/tn/esprit/spring/gestion-station-ski/1.0/gestion-station-ski-1.0.jar

# Verify the JAR file was downloaded correctly
RUN ls -l gestion-station-ski-1.0.jar

# Set the command to run the application
ENTRYPOINT ["java", "-jar", "gestion-station-ski-1.0.jar"]
