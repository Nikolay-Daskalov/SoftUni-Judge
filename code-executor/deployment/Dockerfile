FROM amazoncorretto:17.0.5

MAINTAINER "Nikolay Daskalov"

WORKDIR /code-executor

COPY ./target/code-executor-3.0.1.jar code-executor.jar

ENTRYPOINT ["java", "-jar", "code-executor.jar"]