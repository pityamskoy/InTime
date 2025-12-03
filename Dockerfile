FROM amazoncorretto:25-headless
LABEL authors="ASKekishev and LAGuryanov"

COPY out/artifacts/vSrok_jar2 .
COPY run.sh .

RUN chmod +x run.sh

EXPOSE 8080

ENTRYPOINT ["./run.sh"]