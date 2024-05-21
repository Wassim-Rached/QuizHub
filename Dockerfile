FROM openjdk:17-oracle

# Set the working directory
WORKDIR /app

# Copy the jar file to the container
COPY ./target/quiz-hub-0.0.1-SNAPSHOT.jar /app

# Expose the port
EXPOSE 8080

# Run the jar file
CMD ["java", "-jar", "quiz-hub-0.0.1-SNAPSHOT.jar"]