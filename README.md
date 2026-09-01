# Demo Spring Boot Application

A minimal Spring Boot demo with one REST endpoint.

Prerequisites
- Java 17+
- Maven 3.6+

Build & run (local)
1. Build:
   mvn clean package

2. Run:
   mvn spring-boot:run
   OR
   java -jar target/demo-0.0.1-SNAPSHOT.jar

3. Test:
   curl http://localhost:8080/api/hello
   # Expected JSON:
   # {"message":"Hello, world!","status":"ok"}

Next steps
- I can add a Dockerfile, CI workflow, additional endpoints, or unit tests — tell me which you'd like.
