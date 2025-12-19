FROM maven:4.0.0-rc-5-amazoncorretto-25 as build
LABEL authors="ASKekishev and LAGuryanov"
WORKDIR /app
COPY . .

RUN mvn clean install -DskipTests -B

COPY src ./src

RUN mvn clean package -Dskiptests

FROM amazoncorretto:25-headless
WORKDIR /app

COPY --from=build target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]