FROM ubuntu:latest
LABEL authors="Sumit Choudhary"

# Start with a base image containing Java runtime
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

COPY target/Video-calling-0.0.1-SNAPSHOT.jar /app/Video-calling-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/Video-calling-0.0.1-SNAPSHOT.jar"]


