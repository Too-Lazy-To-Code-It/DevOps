FROM maven:3.6.3-openjdk-17 AS build

WORKDIR /app
COPY . .
RUN mvn clean package

FROM openjdk:11-jre-slim
COPY --from=build /app/target/my-app.jar /app/my-app.jar

