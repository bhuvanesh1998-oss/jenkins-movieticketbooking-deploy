# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the executable JAR file into the container
COPY target/FilmExperiment-0.0.1-SNAPSHOT.jar /app/FilmExperiment-0.0.1-SNAPSHOT.jar

# Make port 8090 available to the world outside this container
EXPOSE 8090

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/FilmExperiment-0.0.1-SNAPSHOT.jar"]
