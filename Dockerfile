# Java 21 Runtime
FROM eclipse-temurin:21-jre

# Working directory
WORKDIR /app

# Copy the application JAR
COPY target/backend-0.0.1-SNAPSHOT.jar app.jar

# Expose Spring Boot port
EXPOSE 8081

# Start the application
ENTRYPOINT ["java","-jar","app.jar"]