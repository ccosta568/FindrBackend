# Step 1: Build the JAR using Gradle
FROM gradle:8.5-jdk17 AS build
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle build -x test

# Step 2: Use a smaller JDK image for the actual app
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

