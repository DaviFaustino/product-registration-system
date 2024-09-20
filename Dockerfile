FROM maven:3.8.5-openjdk-17 as builder
WORKDIR /app
COPY pom.xml .
COPY src /app/src
ARG MAVEN_PROFILE=development
RUN mvn test
RUN mvn package -P${MAVEN_PROFILE} -DskipTests

FROM eclipse-temurin:17.0.12_7-jre
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY --from=builder /app/target/product-registration-system-backend-1.0.1.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
