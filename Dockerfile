FROM openjdk:17-oracle

# Set the working directory
WORKDIR /app

# Copy the jar file to the container
COPY ./target/quiz-hub-0.0.1-SNAPSHOT.jar /app

# Define build argument for version
ARG APP_VERSION

# Pass the version as an environment variable
ENV APP_VERSION=${APP_VERSION}

# Expose the port
EXPOSE 8080

CMD ["java", "-jar", "quiz-hub-0.0.1-SNAPSHOT.jar"]