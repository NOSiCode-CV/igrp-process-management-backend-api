FROM maven:3.9.9-eclipse-temurin-23 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests clean package && ls -lh target

FROM eclipse-temurin:23-jre
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar

COPY certs/irn/*.crt /usr/local/share/ca-certificates/

RUN apt-get update && apt-get install -y ca-certificates && \
    update-ca-certificates && \
    for cert in /usr/local/share/ca-certificates/*.crt; do \
      keytool -importcert -trustcacerts \
      -keystore "$JAVA_HOME/lib/security/cacerts" \
      -storepass changeit -noprompt \
      -alias "$(basename $cert .crt)" \
      -file "$cert"; \
    done && \
    rm -rf /var/lib/apt/lists/*

# Copy only Kafka SSL keystore and truststore
#COPY certs/kafka/client.keystore.jks /etc/kafka/certs/client.keystore.jks
#COPY certs/kafka/client.truststore.jks /etc/kafka/certs/client.truststore.jks
#
#ENV KAFKA_SSL_KEYSTORE_LOCATION=/etc/kafka/certs/client.keystore.jks \
#    KAFKA_SSL_KEYSTORE_PASSWORD=changeit \
#    KAFKA_SSL_KEY_PASSWORD=changeit \
#    KAFKA_SSL_TRUSTSTORE_LOCATION=/etc/kafka/certs/client.truststore.jks \
#    KAFKA_SSL_TRUSTSTORE_PASSWORD=changeit

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

