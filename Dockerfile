FROM openjdk:21-jre-slim

WORKDIR /app

# Copy the pre-built jar file
COPY target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]