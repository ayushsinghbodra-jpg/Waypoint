#syntax=docker/dockerfile:1
FROM eclipse-temurin:26-jdk AS build
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew
RUN --mount=type=cache,target=/root/.gradle ./gradlew build --no-daemon || true 

COPY src src 
RUN --mount=type=cache,target=/root/.gradle ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:26-jre AS run
WORKDIR /app
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
