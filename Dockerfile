FROM openjdk:21-slim

COPY . .

RUN ./mvnw install

CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-Xms1024m -Xmx3g"]
