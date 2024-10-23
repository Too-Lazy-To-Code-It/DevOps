FROM openjdk:8-jdk-alpine
EXPOSE 8082

# Install curl to download the artifact
RUN apk add --no-cache curl

# Download the artifact from Nexus (replace <username> and <password> with your Nexus credentials)
RUN curl -u <redres_nexus>:<4f4949fa-4dd6-35f4-bc47-ed76080ccbba> -O http://192.168.8.104:8081/repository/maven-releases/tn/esprit/spring/gestion-station-ski/1.0/gestion-station-ski-1.0.jar

# Add the downloaded artifact
ADD gestion-station-ski-1.0.jar gestion-station-ski-1.0.jar

ENTRYPOINT ["java","-jar","/gestion-station-ski-1.0.jar"]
