FROM openjdk:8-jdk-alpine
EXPOSE 8089

# Install wget to download the artifact
RUN apk add --no-cache wget

# Create a directory for the JAR file and the pom.xml
RUN mkdir -p /app

# Copy the pom.xml file into the /app directory
COPY pom.xml /app/pom.xml

# Download the artifact into the /app directory
RUN wget --user=admin --password=1 -O /app/gestion-station-ski-1.0.jar http://192.168.223.128:8081/repository/maven-releases/tn/esprit/spring/gestion-station-ski/1.0/gestion-station-ski-1.0.jar

# Verify that the file was downloaded
RUN ls -l /app/gestion-station-ski-1.0.jar /app/pom.xml

# Run the downloaded JAR file from the /app directory
ENTRYPOINT ["java", "-jar", "/app/gestion-station-ski-1.0.jar"]
