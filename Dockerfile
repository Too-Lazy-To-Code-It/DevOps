FROM moenesgueni559/alpine:1.0.0

EXPOSE 8089

RUN apk add --no-cache wget

RUN mkdir -p /app

COPY pom.xml /app/pom.xml

RUN wget --user=admin --password=nexus -O /app/gestion-station-ski-1.0.jar http://10.0.2.15:8081/repository/maven-releases/tn/esprit/spring/gestion-station-ski/1.0/gestion-station-ski-1.0.jar

RUN ls -l /app/gestion-station-ski-1.0.jar /app/pom.xml

ENTRYPOINT ["java", "-jar", "/app/gestion-station-ski-1.0.jar"]