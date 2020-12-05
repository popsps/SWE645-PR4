package com.swe645;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Properties;

public class StudentProducer {
  static final String TOPIC = "survey-data-topic";
  // static final String TOPIC = "survey";
  static final String HOST = "kafka-1:9092";

  public StudentProducer() {
  }

  public static void post(StudentBean student) {
    Properties props = new Properties();
    props.put("bootstrap.servers", HOST);
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    // SSL Support
    props.put("security.protocol", "SSL");
    props.put("ssl.truststore.location", "/var/certs/docker.kafka.client.truststore.jks");
    props.put("ssl.truststore.password", "swe645");
    props.put("ssl.truststore.type", "JKS");
    props.put("ssl.key.password", "swe645");
    props.put("ssl.endpoint.identification.algorithm", "");

    Gson gson = new Gson();
    String data = gson.toJson(student);
    ProducerRecord producerRecord = new ProducerRecord(TOPIC, "swe645", data);
    KafkaProducer kafkaProducer = new KafkaProducer(props);
    kafkaProducer.send(producerRecord);
    kafkaProducer.close();
  }
}
