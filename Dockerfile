FROM amazoncorretto:25-headless
LABEL authors="ASKekishev and LAGuryanov"

WORKDIR /app
COPY out/artifacts/vSrok_jar ./

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/vSrok.jar"]