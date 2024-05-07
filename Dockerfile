FROM eclipse-temurin:21.0.3_9-jre-ubi9-minimal
COPY ./target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
