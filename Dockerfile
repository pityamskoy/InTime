FROM maven:4.0.0-rc-5-amazoncorretto-25 as build
LABEL authors="ASKekishev and LAGuryanov"

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -Dskiptests

FROM amazoncorretto:25-headless

COPY --from=build target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]