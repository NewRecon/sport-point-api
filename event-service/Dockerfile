FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY target/event-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-XX:+UseSerialGC", "-jar", "app.jar"]