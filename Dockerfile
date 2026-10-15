# Stage 1: Build & Package application using Maven
FROM maven:3.9-eclipse-temurin-21-alpine AS builder
WORKDIR /build

# Copy Maven wrapper & pom.xml first to leverage Docker layer caching for dependencies
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Copy source code and build the production JAR without re-running tests
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Stage 2: Create lightweight runtime image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
VOLUME /tmp

# Copy compiled JAR from Stage 1 builder
COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]