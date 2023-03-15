FROM maven:3.8.2-jdk-11-slim as builder
WORKDIR /build
COPY . /build
RUN mvn clean package

FROM openjdk:11.0.12-slim
ARG min_memory=256m
ARG max_memory=512m
ENV JAVA_OPTS="-Xms${min_memory} -Xmx${max_memory}"
#ENV VISUALVM_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9010 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
ENV VISUALVM_OPTS=""
VOLUME /tmp
WORKDIR /opt
COPY --from=builder /build/target/*.jar /opt/app.jar
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=development $JAVA_OPTS $VISUALVM_OPTS -jar "/opt/app.jar"
