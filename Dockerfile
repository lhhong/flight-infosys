FROM maven

ADD . /app
WORKDIR /app

RUN mvn install && mvn package

ENTRYPOINT ["java", "-jar", "target/flight-infosys-server-jar-with-dependencies.jar"]

CMD []
