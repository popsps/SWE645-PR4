package com.swe645;

import com.google.gson.Gson;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;

public class SampleProducer {
  public SampleProducer() {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
    StudentBean student = new StudentBean(234324, "Ray", "Danovan", "Fairfax",
        22030, "fairfax", "VA", "202", "mike@gmail.com", timestamp);
    Gson gson = new Gson();
    String data = gson.toJson(student);
    System.out.println("data sent: " + data);
    ProducerRecord producerRecord = new ProducerRecord("test1", "swe645", data);

    KafkaProducer kafkaProducer = new KafkaProducer(props);
    kafkaProducer.send(producerRecord);
    kafkaProducer.close();
  }

}
