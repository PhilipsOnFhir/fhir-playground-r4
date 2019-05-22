FROM openjdk:8-jdk-alpine
LABEL maintainer="bas.van.den.heuvel@philips.com"
VOLUME /tmp
EXPOSE 9080:80
ARG JAR_FILE=./target/fhir-cast-hub-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]