FROM maven:3.9.9-eclipse-temurin-21-noble AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENV BACKEND_PORT=8080
EXPOSE ${BACKEND_PORT}

ENTRYPOINT ["java","-jar","app.jar"]
