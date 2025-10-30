FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
#Copy Maven wrapper to avoid installing Maven in container
COPY mvnw .
COPY .mvn .mvn
#Copy necessary poms for the module
COPY pom.xml .
COPY order-service/pom.xml ./order-service/
COPY order-processor/pom.xml ./order-processor/
COPY event-schemas/pom.xml ./event-schemas/
# Copy necessary source code
COPY order-service/src ./order-service/src
COPY order-processor/src ./order-processor/src
COPY event-schemas/src ./event-schemas/src
#Run wrapper to delete old jars and create a new on only for specific module
RUN ./mvnw clean package -pl order-processor -am -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/order-processor/target/order-processor-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]