FROM maven:3.8.4-openjdk-17-slim
WORKDIR /app
COPY . .
RUN sleep 60 && mvn package
EXPOSE 8082
ENTRYPOINT [ "java", "-jar", "target/func-0.0.1-SNAPSHOT.jar" ]

