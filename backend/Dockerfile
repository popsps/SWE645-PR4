FROM maven:alpine AS maven_builder
WORKDIR /workspace
ADD pom.xml ./
COPY . .
RUN mvn clean package

FROM tomcat:alpine
COPY --from=maven_builder /workspace/target/swe645_hw4-1.0.war /usr/local/tomcat/webapps
EXPOSE 8080

