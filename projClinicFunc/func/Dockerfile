FROM maven:3.8.4-openjdk-17-slim
WORKDIR /app
COPY . .
RUN mvn package
EXPOSE 8082
ENTRYPOINT [ "java", "-jar", "target/clinic-0.0.1-SNAPSHOT.jar" ]

