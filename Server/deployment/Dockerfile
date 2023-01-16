FROM amazoncorretto:17.0.5

MAINTAINER "Nikolay Daskalov"

WORKDIR /app

COPY ./target/server-3.0.1.jar server.jar

ENTRYPOINT ["java", "-jar", "server.jar"]