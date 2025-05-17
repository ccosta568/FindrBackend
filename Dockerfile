# Build stage
FROM gradle:8.5-jdk17 AS build
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle build -x test

# Run stage
FROM openjdk:17-slim

# Install Chrome and ChromeDriver dependencies
RUN apt-get update && apt-get install -y \
    chromium \
    chromium-driver \
    wget \
    unzip \
    xvfb \
    && rm -rf /var/lib/apt/lists/*

# Set up Chrome options
ENV CHROME_OPTIONS="--headless,--disable-gpu,--no-sandbox,--disable-dev-shm-usage"

# Create app directory
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]