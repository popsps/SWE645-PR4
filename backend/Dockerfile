FROM maven:alpine AS maven_builder
WORKDIR /workspace
ADD pom.xml ./
COPY . .
RUN mvn clean package

FROM tomcat:alpine
COPY --from=maven_builder /workspace/target/swe645_hw4-1.0.war /usr/local/tomcat/webapps
COPY --from=maven_builder /workspace/src/main/resources/docker.kafka.client.truststore.jks /var/certs/docker.kafka.client.truststore.jks
EXPOSE 8080

