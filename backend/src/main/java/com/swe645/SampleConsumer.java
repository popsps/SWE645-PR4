package com.swe645;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;


import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class SampleConsumer {
  static final String TOPIC = "survey";
  static final String GROUP = "test1_group10";

  public SampleConsumer() {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9093");
    props.put("group.id", GROUP);
    props.put("enable.auto.commit", true);
    props.put("auto.commit.interval.ms", "1000");
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("auto.offset.reset", "earliest");
    KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
//    consumer.subscribe(Arrays.asList("foo", "bar"));
    consumer.subscribe(Arrays.asList(TOPIC));

    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(2000));
//    if (records.isEmpty()) {
//      consumer.seekToBeginning(consumer.assignment());
//    }
    consumer.seekToBeginning(consumer.assignment());
    while (true) {
      for (ConsumerRecord<String, String> record : records) {
        System.out.printf("offset: %d, key: %s, value: %s\n", record.offset(), record.key(), record.value());
      }
      records = consumer.poll(Duration.ofMillis(100));
    }
//    consumer.commitSync(); manually commit
  }
}
