FROM maven:3.9.9-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/ app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]