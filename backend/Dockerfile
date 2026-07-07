FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew

CMD ["./gradlew", "quarkusDev", "-Dquarkus.http.host=0.0.0.0"]
