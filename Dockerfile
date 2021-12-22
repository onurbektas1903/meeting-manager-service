FROM openjdk:11.0.6-jre
RUN mkdir /opt/app
COPY ./target/meeting-manager-service-0.0.1-SNAPSHOT.jar /opt/app
WORKDIR /opt/app
ARG PROFILES
ARG JAVA_OPTIONS="-Dfile.encoding=UTF-8 -Dspring.profiles.active=test -Dproduct.version.version=0.0.1-SNAPSHOT"
ENV JAVA_OPTS=${JAVA_OPTIONS}
EXPOSE 8080
CMD ["sh","-c","java ${JAVA_OPTS} -jar meeting-manager-service-0.0.1-SNAPSHOT.jar"]