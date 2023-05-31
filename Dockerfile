FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Generate the keystore
RUN keytool -genkeypair -alias localhost_ssl -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.jks -validity 365 -storepass password -keypass password -dname "CN=Unknown, OU=Unknown, O=Unknown, L=Unknown, ST=Unknown, C=Unknown"

# Gradle clean and build the project, skip tests
RUN ./gradlew clean build -x test

# Move the keystore to /resources
RUN mv keystore.jks src/main/resources/keystore/

# Add the application's jar to the container
RUN mv build/libs/cm-0.0.1.jar app.jar

# Make port 8443 available to the world outside this container
EXPOSE 8443

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
