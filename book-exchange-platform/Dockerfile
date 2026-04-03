FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Render is currently using the repository root as build context,
# so copy project files from the book-exchange-platform subdirectory.
COPY book-exchange-platform/pom.xml .
COPY book-exchange-platform/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/target/book-exchange-platform-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "if [ -z \"$SPRING_DATASOURCE_URL\" ] && [ -n \"$DATABASE_URL\" ]; then export SPRING_DATASOURCE_URL=\"$DATABASE_URL\"; fi; if [ -n \"$SPRING_DATASOURCE_URL\" ] && [ \"${SPRING_DATASOURCE_URL#jdbc:}\" = \"$SPRING_DATASOURCE_URL\" ]; then case \"$SPRING_DATASOURCE_URL\" in postgres://*) export SPRING_DATASOURCE_URL=\"jdbc:postgresql://${SPRING_DATASOURCE_URL#postgres://}\" ;; postgresql://*) export SPRING_DATASOURCE_URL=\"jdbc:postgresql://${SPRING_DATASOURCE_URL#postgresql://}\" ;; *) export SPRING_DATASOURCE_URL=\"jdbc:${SPRING_DATASOURCE_URL}\" ;; esac; fi; exec java ${JAVA_OPTS} -Dserver.port=${PORT:-8080} -jar app.jar"]
