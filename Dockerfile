# Use lightweight JDK
FROM eclipse-temurin:17-jdk-jammy as build

WORKDIR /app
COPY target/*.jar app.jar

# Expose port 8080 for Cloud Run
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java","-jar","/app/app.jar"]
