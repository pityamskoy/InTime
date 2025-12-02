FROM amazoncorretto:25-headless
LABEL authors="ASKekishev and LAGuryanov"

COPY out/artifacts/vSrok_jar2 .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./vSrok.jar"]