//package com.swe645;
//
//import org.apache.kafka.clients.consumer.ConsumerGroupMetadata;
//import org.apache.kafka.clients.producer.Callback;
//import org.apache.kafka.clients.producer.Producer;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.clients.producer.RecordMetadata;
//import org.apache.kafka.common.Metric;
//import org.apache.kafka.common.MetricName;
//import org.apache.kafka.common.PartitionInfo;
//import org.apache.kafka.common.errors.ProducerFencedException;
//
//import java.time.Duration;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Future;
//
//public class KafkaProducer implements Producer {
//  @Override
//  public void initTransactions() {
//
//  }
//
//  @Override
//  public void beginTransaction() throws ProducerFencedException {
//
//  }
//
//  @Override
//  public void commitTransaction() throws ProducerFencedException {
//
//  }
//
//  @Override
//  public void abortTransaction() throws ProducerFencedException {
//
//  }
//
//  @Override
//  public Future<RecordMetadata> send(ProducerRecord producerRecord) {
//    return null;
//  }
//
//  @Override
//  public Future<RecordMetadata> send(ProducerRecord producerRecord, Callback callback) {
//    return null;
//  }
//
//  @Override
//  public void flush() {
//
//  }
//
//  @Override
//  public List<PartitionInfo> partitionsFor(String s) {
//    return null;
//  }
//
//  @Override
//  public Map<MetricName, ? extends Metric> metrics() {
//    return null;
//  }
//
//  @Override
//  public void close() {
//
//  }
//
//  @Override
//  public void close(Duration duration) {
//
//  }
//
//  @Override
//  public void sendOffsetsToTransaction(Map map, ConsumerGroupMetadata consumerGroupMetadata) throws ProducerFencedException {
//
//  }
//
//  @Override
//  public void sendOffsetsToTransaction(Map map, String s) throws ProducerFencedException {
//
//  }
//}
