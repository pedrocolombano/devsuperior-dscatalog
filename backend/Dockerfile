FROM openjdk:11
VOLUME /tmp
EXPOSE 8080
ADD ./target/dscatalog-1.0.0-SNAPSHOT.jar dscatalog.jar
ENTRYPOINT ["java","-jar","/dscatalog.jar"]