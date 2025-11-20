FROM gradle:7.4.0-jdk17

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew || true

RUN ./gradlew build -x test

EXPOSE 7070

CMD ["java", "-jar", "build/libs/java-project-72-1.0-SNAPSHOT.jar"]
