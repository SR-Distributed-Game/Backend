FROM openjdk:21-slim

COPY . .

RUN ./mvnw install

CMD ["./mvnw", "spring-boot:run"]
