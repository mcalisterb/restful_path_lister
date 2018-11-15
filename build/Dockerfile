FROM openjdk:8-jdk-alpine
RUN apk add maven
RUN mkdir -p /tmp/spring_rest/src/main/java/hello
COPY ./src/pom.xml /tmp/spring_rest/
COPY ./src/Application.java /tmp/spring_rest/src/main/java/hello/
RUN cd /tmp/spring_rest && mvn package
RUN mkdir -p /mnt/localfs
CMD cd /tmp/spring_rest && mvn spring-boot:run

VOLUME /mnt/localfs/



#CMD touch /tmp/test.log && tail -f /tmp/test.log
