# Use Ubuntu Minimal as the base image
FROM ubuntu:22.04

# Install OpenJDK 8 JRE
RUN apt-get update && \
    apt-get install -y openjdk-8-jre-headless && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory to /app
WORKDIR /app

# Copy the JAR file to the container
COPY book.jar .

# Set the default command to run the JAR file
CMD ["java", "-jar", "book.jar"]
