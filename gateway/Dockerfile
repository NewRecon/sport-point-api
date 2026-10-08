FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY target/gateway-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xmx768m", "-Xms384m", "-XX:+UseSerialGC", "-jar", "app.jar"]