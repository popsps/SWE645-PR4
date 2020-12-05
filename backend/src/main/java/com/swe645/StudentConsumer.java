package com.swe645;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class StudentConsumer {
  public StudentConsumer() {
  }

  // static final String TOPIC = "survey-data-topic";
  static final String TOPIC = "survey";
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

      // SSL Support
      props.put("security.protocol", "SSL");
      props.put("ssl.truststore.location", "/var/certs/docker.kafka.client.truststore.jks");
      props.put("ssl.truststore.password", "swe645");
      props.put("ssl.truststore.type", "JKS");
      props.put("ssl.key.password", "swe645");
      props.put("ssl.endpoint.identification.algorithm", "");
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
        if (!students.isEmpty()) {
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
