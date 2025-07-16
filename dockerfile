# Use an official OpenJDK runtime as the base image
FROM openjdk:22-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the Java source file
COPY TCPServer.java .

# Compile the Java program
RUN javac TCPServer.java

# Expose the port (Render will override with PORT env variable)
EXPOSE 10000

# Command to run the server
CMD ["java", "TCPServer"]