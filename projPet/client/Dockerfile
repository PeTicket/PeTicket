FROM maven:3.8.4-openjdk-17-slim
WORKDIR /app
COPY . .
RUN mvn clean package
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "target/client-0.0.1-SNAPSHOT.jar" ]

