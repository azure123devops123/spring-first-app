###################
# Build Stage - 1 #
###################
# Use the official openjkd image as the base image
FROM openjdk:23-jdk-slim-bullseye as build

# Set the working directory inside the container
WORKDIR /app/source

# Add non root user
# RUN groupadd -r nonroot && useradd -u 1002 -r -g nonroot nonroot
# RUN useradd -u 1001 nonroot

# Copy Source Code (Application files) into the working directory.
COPY . /app/source

# Build the project using Maven tool.
RUN ./mvnw clean package

###################
# Build Stage - 2 #
###################
# Use the official minimal Linux, OpenJDK-based runtime DISTROLESS image as the base image only java runtime environment
FROM gcr.io/distroless/java17-debian12 as runtime

# Set the working directory inside the container
WORKDIR /app

# Copy the binary artifact from previous build into to the working directory
COPY --from=build /etc/passwd /etc/passwd

# Use nonroot user
# USER nonroot

# Copy the JAR file of the build artifact from previous stage into the container's working directory
COPY --link --from=build /app/source/target/*.jar /app/app.jar

# Expose the port on which your application listens
EXPOSE 8080

# Run the run a Java application using JAR file.
ENTRYPOINT [ "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar" ]
# CMD ["java", "-jar","/app/app.jar"]