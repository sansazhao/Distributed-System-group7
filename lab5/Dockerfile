FROM openjdk:8
VOLUME /tmp
ADD ./target/dsgroup7-1.0-SNAPSHOT.jar app.jar
EXPOSE 8081
RUN sh -c 'touch /app.jar'
CMD ["java","-jar","/app.jar"]
