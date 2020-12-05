package com.swe645;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;


import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class SampleConsumer {
  static final String TOPIC = "survey-data-topic";
  static final String GROUP = "test1_group11";

  public SampleConsumer() {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9093");
    props.put("group.id", GROUP);
    props.put("enable.auto.commit", true);
    props.put("auto.commit.interval.ms", "1000");
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("auto.offset.reset", "earliest");

    Path path = Paths.get("");
    URL tc = this.getClass().getClassLoader().getResource("docker.kafka.client.truststore.jks");
    System.out.println("tc: " + tc.getFile().substring(1));
    // ssl support
    props.put("security.protocol", "SSL");
    props.put("ssl.truststore.location", "C:\\Users\\MarsS\\Dropbox\\GMU\\fall 2020\\SWE645\\hw4\\SWE645-PR4\\kafka_test\\certs\\docker.kafka.client.truststore.jks");
    props.put("ssl.truststore.password", "swe645");
    props.put("ssl.truststore.type", "JKS");
    props.put("ssl.key.password", "swe645");
    props.put("ssl.endpoint.identification.algorithm", "");

    KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
//    consumer.subscribe(Arrays.asList("foo", "bar"));
    consumer.subscribe(Arrays.asList(TOPIC));

    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(3000));
//    if (records.isEmpty()) {
//      consumer.seekToBeginning(consumer.assignment());
//    }
    consumer.seekToBeginning(consumer.assignment());
    while (true) {
      for (ConsumerRecord<String, String> record : records) {
        System.out.printf("offset: %d, key: %s, value: %s\n", record.offset(), record.key(), record.value());
      }
      records = consumer.poll(Duration.ofMillis(1000));
    }
//    consumer.commitSync(); manually commit
  }
}
