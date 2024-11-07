FROM moenesgueni559/alpine:1.0.0

EXPOSE 8082

ADD target/DevOps_Project-1.0.jar DevOps_Project-1.0.jar

ENTRYPOINT ["java","-jar","/DevOps_Project-1.0.jar"]