FROM openjdk:8-jdk-alpine
EXPOSE 8089


RUN apk add --no-cache wget


RUN mkdir -p /app


COPY pom.xml /app/pom.xml


RUN wget --user=admin --password=Nexus -O /app/gestion-station-ski-1.0.jar http://192.168.1.12:8081//repository/maven-releases/tn/esprit/spring/gestion-station-ski/1.0/gestion-station-ski-1.0.jar


RUN ls -l /app/gestion-station-ski-1.0.jar /app/pom.xml


ENTRYPOINT ["java", "-jar", "/app/gestion-station-ski-1.0.jar"]