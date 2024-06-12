# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 as build

WORKDIR /app

# Copy the necessary files
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src ./src

# Run Maven to build the application
RUN mvn clean install -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/book-api-0.0.1-SNAPSHOT.jar ./book-api.jar

EXPOSE 8080

CMD ["java", "-jar", "./book-api.jar"]
