FROM openjdk:8
VOLUME /tmp
ADD ./target/app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
RUN sh -c 'touch /app.jar'
CMD ["java","-jar","/app.jar"]
