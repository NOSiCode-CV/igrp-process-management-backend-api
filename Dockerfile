FROM maven:3.9.16-eclipse-temurin-26 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -Dmaven.test.skip=true clean package && ls -lh target

FROM eclipse-temurin:26-jre
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

