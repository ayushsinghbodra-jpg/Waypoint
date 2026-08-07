# syntax=docker/dockerfile:1

# ---- Build stage ----
# NOTE: verify this tag actually exists for Java 26 at build time —
# it's a very new JDK release, so eclipse-temurin may not have published
# an image for it yet. If the pull fails, check
# https://hub.docker.com/_/eclipse-temurin/tags and pin to whatever's
# actually available (worst case, drop to Java 21/25 LTS until 26 images land).
FROM eclipse-temurin:26-jdk AS build

WORKDIR /app

# Copy Gradle wrapper + build files first so dependency resolution is
# cached in its own Docker layer — only re-runs when build.gradle actually
# changes, not on every source-code edit.
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon || true

# Now copy actual source and build the jar
COPY src src
RUN ./gradlew bootJar --no-daemon -x test

# ---- Run stage ----
# JRE only, not JDK — smaller image, no compiler/build tools shipped
FROM eclipse-temurin:26-jre AS run

WORKDIR /app

# Run as non-root — do not skip this for anything you'd call production-grade
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

# Graceful shutdown needs server.shutdown=graceful in application.yaml to
# actually take effect — this just gives the JVM a clean SIGTERM to react to.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]