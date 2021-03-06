# SWE645-PROJECT4

## [EC2 Link](http://ec2-3-239-72-137.compute-1.amazonaws.com)

> ## **A STAND ALONE APPLICATION WITH THE EXTRA CREDIT INTEGRATION IS PROVIDED**


This project is a container orchestration of 3 services. A frontend web application implemented using `Angular` and `nginx`. A backend application implemented using `JAVA EE` and `Tomcat` to handle REST API requests. And finally a persistent `Kafka` messaging service that acts as a persistent database.

## BUILD

To build and run this project you will need to have `Docker` installed on your machine. We will be using `Docker Swarm` for orchestration of services. To build and run this project simply run `docker stack deploy -c ./docker-compose.yml swe645`. This will create a frontend, a backend, 3 brokers and 3 zookeepers.

For this project we will be using 1 Amazon `EC2` instance to serve all the services. The setup for this is exactly as what we have previously constructed and can be found at [SWE645-HW2](https://github.com/popsps/SWE645-group-project).

# UNDERSTANDING PROJECT FROM SCRATCH

## JENKINS

To build and run this project we will be using `Jenkins` to automate the build. To achieve this goal we simply run this script in our Jenkins.

```sh
docker service rm $(docker service ls -q) || true # remove all previous services
docker-compose up -d --build # make images
docker-compose down --volumes # remove container created by compose
docker stack deploy -c ./docker-compose.yml swe645 # run the container ochestration stack
```

There are two major changes in this project in comparison to the pervious project. One is the addition of `Kafa` and two is the modification of the backend. We first discuss our configuration for the `Kafka` services and then dive into our backend.(There is no change to the frontend application).

## KAFKA

### **`docker-compose.yml`**

```yml
version: "3"

volumes:
  kafka-data-1:
  kafka-data-2:
  kafka-data-3:
  zookeeper-data-1:
  zookeeper-data-2:
  zookeeper-data-3:
  certs:

networks:
  swenet:

services:
  swe645-backend:
    image: 127.0.0.1:5000/tomcat-backend
    build: ./backend
    container_name: swe645-backend
    ports:
      - 8081:8080
    networks:
      - swenet

  swe645-frontend:
    image: 127.0.0.1:5000/nginx-frontend
    build: ./frontend
    container_name: swe645-frontend
    ports:
      - 80:80
    networks:
      - swenet

  swe645-zookeeper-1:
    image: wurstmeister/zookeeper:latest
    container_name: swe645-zookeeper-1
    volumes:
      - zookeeper-data-1:/opt/zookeeper-3.4.13/data
    ports:
      - 2181:2181
    networks:
      - swenet
    environment:
      ZOOKEEPER_SERVER_ID: 1
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
      ZOOKEEPER_INIT_LIMIT: 5
      ZOOKEEPER_SYNC_LIMIT: 2
      ZOOKEEPER_SERVERS: swe645-zookeeper-1:2887:3887;swe645-zookeeper-2:2888:3888;swe645-zookeeper-3:2889:3889

  swe645-zookeeper-2:
    image: wurstmeister/zookeeper:latest
    container_name: swe645-zookeeper-2
    volumes:
      - zookeeper-data-2:/opt/zookeeper-3.4.13/data
    ports:
      - 2182:2182
    networks:
      - swenet
    environment:
      ZOOKEEPER_SERVER_ID: 2
      ZOOKEEPER_CLIENT_PORT: 2182
      ZOOKEEPER_TICK_TIME: 2000
      ZOOKEEPER_INIT_LIMIT: 5
      ZOOKEEPER_SYNC_LIMIT: 2
      ZOOKEEPER_SERVERS: swe645-zookeeper-1:2887:3887;swe645-zookeeper-2:2888:3888;swe645-zookeeper-3:2889:3889

  swe645-zookeeper-3:
    image: wurstmeister/zookeeper:latest
    container_name: swe645-zookeeper-3
    volumes:
      - zookeeper-data-3:/opt/zookeeper-3.4.13/data
    ports:
      - 2183:2183
    networks:
      - swenet
    environment:
      ZOOKEEPER_SERVER_ID: 3
      ZOOKEEPER_CLIENT_PORT: 2183
      ZOOKEEPER_TICK_TIME: 2000
      ZOOKEEPER_INIT_LIMIT: 5
      ZOOKEEPER_SYNC_LIMIT: 2
      ZOOKEEPER_SERVERS: swe645-zookeeper-1:2887:3887;swe645-zookeeper-2:2888:3888;swe645-zookeeper-3:2889:3889

  swe645-kafka-1:
    image: wurstmeister/kafka:latest
    container_name: swe645-kafka-1
    networks:
      swenet:
        aliases:
          - kafka-1
    ports:
      - 9093:9093
    volumes:
      - kafka-data-1:/kafka/kafka-logs
      - certs:/certs
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: swe645-zookeeper-1:2181, swe645-zookeeper-2:2182, swe645-zookeeper-3:2183
      KAFKA_CONTROLLER_SHUTDOWN_ENABLE: "true"
      KAFKA_LISTENERS: INSIDE://:9092,OUTSIDE://:9093
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INSIDE:PLAINTEXT,OUTSIDE:SSL
      KAFKA_INTER_BROKER_LISTENER_NAME: INSIDE
      KAFKA_LOG_RETENTION_BYTES: -1
      KAFKA_LOG_RETENTION_DAYS: -1
      KAFKA_LOG_DIRS: /kafka/kafka-logs
      KAFKA_ADVERTISED_LISTENERS: INSIDE://:9092,OUTSIDE://localhost:9093
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"
      KAFKA_CREATE_TOPICS: "testswe"

  swe645-kafka-2:
    image: wurstmeister/kafka:latest
    container_name: swe645-kafka-2
    networks:
      swenet:
        aliases:
          - kafka-2
    volumes:
      - kafka-data-2:/kafka/kafka-logs
    environment:
      KAFKA_BROKER_ID: 2
      KAFKA_ZOOKEEPER_CONNECT: swe645-zookeeper-1:2181, swe645-zookeeper-2:2182, swe645-zookeeper-3:2183
      KAFKA_CONTROLLER_SHUTDOWN_ENABLE: "true"
      KAFKA_LISTENERS: INSIDE://:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INSIDE:PLAINTEXT,OUTSIDE:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: INSIDE
      KAFKA_LOG_RETENTION_BYTES: -1
      KAFKA_LOG_RETENTION_DAYS: -1
      KAFKA_LOG_DIRS: /kafka/kafka-logs

  swe645-kafka-3:
    image: wurstmeister/kafka:latest
    container_name: swe645-kafka-3
    networks:
      swenet:
        aliases:
          - kafka-3
    volumes:
      - kafka-data-3:/kafka/kafka-logs
    environment:
      KAFKA_BROKER_ID: 3
      KAFKA_ZOOKEEPER_CONNECT: swe645-zookeeper-1:2181, swe645-zookeeper-2:2182, swe645-zookeeper-3:2183
      KAFKA_CONTROLLER_SHUTDOWN_ENABLE: "true"
      KAFKA_LISTENERS: INSIDE://:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INSIDE:PLAINTEXT,OUTSIDE:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: INSIDE
      KAFKA_LOG_RETENTION_BYTES: -1
      KAFKA_LOG_RETENTION_DAYS: -1
```

### HOW DOES THE YML FILE WORK

We create 3 `kafka brokers` and 3 `zookeepers`. Each `kafka broker` gets a alias name on the network that lets it to be able to connect to other brokers. We define 2 protocols. One for inside communications between brokers. It is called `INSIDE`. One is for outside communications to brokers. It is called `OUTSIDE`. The `INSIDE` is used for inner communication between brokers as well as backend to brokers using the port `9092`. The `OUTSIDE` is used for testing our application on local machine and it uses port `9093`.

### Persistency

We use `volumes` to save data to persistent storage. The logs will be saved at `/kafka/kafka-logs`. We set `KAFKA_LOG_RETENTION_DAYS` to -1 to make sure the data would be saved for ever. 




## JAVA BACKEND

To address the **FIFO** we need to produce all messages on the same partition. To achieve that we are using the **same key** for all the records. There are three files that you need to pay special attention to.`./backend/src/java/com/swe645/StudentProducer.java` and `./backend/src/java/com/swe645/StudentConsumcer.java` and `./backend/src/java/com/swe645/FormServlet.java`. `FormServlet.java` is in charge of handling the get/post/get_by_id requests from the frontend. On `post` it handles it by using methods in the `StudentProducer.java` and on `get/get_by_id` it handles it by methods in `StudentConsumer.java`. **We read and write JSON strings into the KAFKA services.**

### **`StudentProducer.java`**

```java
 public class StudentProducer {
  static final String TOPIC = "survey-data-topic";
  static final String HOST = "kafka-1:9092";

  public StudentProducer() {
  }

  public static void post(StudentBean student) {
    Properties props = new Properties();
    props.put("bootstrap.servers", HOST);
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    Gson gson = new Gson();
    String data = gson.toJson(student);
    ProducerRecord producerRecord = new ProducerRecord(TOPIC, "swe645", data);
    KafkaProducer kafkaProducer = new KafkaProducer(props);
    kafkaProducer.send(producerRecord);
    kafkaProducer.close();
  }
}
```

### **`StudentConsumer.java`**

```java
public class StudentConsumer {
  public StudentConsumer() {
  }

  static final String TOPIC = "survey-data-topic";
  static final String GROUP = "survey_group";
  static final String HOST = "kafka-1:9092";

  public static StudentBean getById(int id) {
    try {
      Properties props = new Properties();
      props.put("bootstrap.servers", HOST);
      props.put("group.id", GROUP);
      props.put("enable.auto.commit", true);
      props.put("auto.commit.interval.ms", "1000");
      props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
      props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
      props.put("auto.offset.reset", "earliest");
      KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
      consumer.subscribe(Arrays.asList(TOPIC));

      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1500));
      if (records.isEmpty()) {
        consumer.seekToBeginning(consumer.assignment());
      }
      int i = 0;
      while (i < 5000) {
        for (ConsumerRecord<String, String> record : records) {
          try {
            Gson gson = new Gson();
            StudentBean student = gson.fromJson(record.value(), StudentBean.class);
            if (student.getStudentID() == id) {
              consumer.close();
              return student;
            }

          } catch (Exception e) {
            System.out.println("Something went wrong consuming kafka data");
            e.printStackTrace();
          }
        }
        records = consumer.poll(Duration.ofMillis(100));
        i++;
      }
      consumer.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static List<StudentBean> getAllSurveys() {
    List<StudentBean> students = new ArrayList<>();
    try {
      Properties props = new Properties();
      props.put("bootstrap.servers", HOST);
      props.put("group.id", GROUP);
      props.put("enable.auto.commit", true);
      props.put("auto.commit.interval.ms", "1000");
      props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
      props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
      props.put("auto.offset.reset", "earliest");
      KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
      consumer.subscribe(Arrays.asList(TOPIC));
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1500));
      if (records.isEmpty()) {
        consumer.seekToBeginning(consumer.assignment());
      }
      int i = 0;
      while (i < 5000) {
        for (ConsumerRecord<String, String> record : records) {
          try {
            Gson gson = new Gson();
            StudentBean student = gson.fromJson(record.value(), StudentBean.class);
            students.add(student);
            System.out.println(student.getStudentID() + " " + student.getfName() + " " + student.getlName() + "consumed");
          } catch (Exception e) {
            System.out.println("Something went wrong consuming kafka data");
            e.printStackTrace();
          }
        }
        if(!students.isEmpty()){
          consumer.close();
          return students;
        }
        records = consumer.poll(Duration.ofMillis(100));
        i++;
      }
      consumer.close();
      System.out.println("Consumer connection is closed");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return students;
  }
}
```

## EXTRA CREDIT

### BUILD CERTIFICATES

- Run `./generate-docker-kafka-ssl-certs.sh` to generate all certificates in the a directory called `certs`.
- Run `docker-compose -f docker-compose-single-node_ssl.yml up` to create 2 instances with `SSL` support.
- Run `/backend/src/java/com/swe645/testKafkaProducer.java` to produce a record using `SSL` 
- Run `/backend/src/java/com/swe645/testKafkaConsumer.java` to consume records using `SSL`

### SSL SUPPORT CODE ADDITION 

> To see complete code refer to each file

### **`docker-compose-single-node_ssl.yml.java`**

```yml
version: "3"
  swe645-kafka:
    volumes:
      - ./certs:/certs
    environment:
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: INSIDE:PLAINTEXT,OUTSIDE:SSL
      # SSL SECURITY
      KAFKA_SSL_KEYSTORE_LOCATION: '/certs/docker.kafka.server.keystore.jks'
      KAFKA_SSL_KEYSTORE_PASSWORD: 'swe645'
      KAFKA_SSL_KEY_PASSWORD: 'swe645'
      KAFKA_SSL_TRUSTSTORE_LOCATION: '/certs/docker.kafka.server.truststore.jks'
      KAFKA_SSL_TRUSTSTORE_PASSWORD: 'swe645'
      KAFKA_SSL_CLIENT_AUTH: 'none'
      KAFKA_SSL_ENDPOINT_IDENTIFICATION_ALGORITHM: ''
```
### **`/backend/src/java/com/swe645/SampleProducer.java`**

```java
// SSL Support
props.put("security.protocol", "SSL");
props.put("ssl.truststore.location", "C:\\Users\\MarsS\\Dropbox\\GMU\\fall 2020\\SWE645\\hw4\\SWE645-PR4\\kafka_test\\certs\\docker.kafka.client.truststore.jks");
props.put("ssl.truststore.password", "swe645");
props.put("ssl.truststore.type", "JKS");
props.put("ssl.key.password", "swe645");
props.put("ssl.endpoint.identification.algorithm", "");
```

### **`/backend/src/java/com/swe645/SampleConsumer.java`**

```java
// ssl support
props.put("security.protocol", "SSL");
props.put("ssl.truststore.location", "C:\\Users\\MarsS\\Dropbox\\GMU\\fall 2020\\SWE645\\hw4\\SWE645-PR4\\kafka_test\\certs\\docker.kafka.client.truststore.jks");
props.put("ssl.truststore.password", "swe645");
props.put("ssl.truststore.type", "JKS");
props.put("ssl.key.password", "swe645");
props.put("ssl.endpoint.identification.algorithm", "");
```


