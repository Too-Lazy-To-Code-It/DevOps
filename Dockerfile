FROM moenesgueni559/alpine:1.0.0

EXPOSE 8089

ADD target/gestion-station-ski-1.0.jar gestion-station-ski-1.0.jar

ENTRYPOINT ["java","-jar","/gestion-station-ski-1.0.jar"]