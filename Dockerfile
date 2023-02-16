FROM maven:3.8.2-jdk-11-slim as builder
WORKDIR /build
COPY . /build
RUN mvn clean package

FROM openjdk:11.0.12-slim
VOLUME /tmp
WORKDIR /opt
COPY --from=builder /build/target/*.jar /opt/app.jar
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar -Dspring.profiles.active=development "/opt/app.jar"
