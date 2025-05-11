# Stage 1 - Build the Jar using maven
# pull maven and jdk-17 image
FROM maven:3.8.3-openjdk-17 AS builder

# Create a folder where the app code will be stored
WORKDIR /project

# Copy the source code from your Host machine to your container
COPY . /project

# Create Jar file
RUN mvn clean install -DskipTests=true

# Stage 2- execute JAR file from the above stage
# pull jdk-small size
FROM openjdk:17-alpine

# Create a new folder where the app code will be stored
WORKDIR /app

# Copy the app jar file and rename the jar file name
COPY --from=builder /project/target/*.jar /app/spring-app.jar

# Run the  application when the container starts
CMD ["java", "-jar", "spring-app.jar"]
