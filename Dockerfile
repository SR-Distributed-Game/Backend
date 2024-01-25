FROM openjdk:21-slim

COPY . .

CMD ["./mvnw", "spring-boot:run"]
