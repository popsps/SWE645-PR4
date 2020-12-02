package com.swe645;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import javax.persistence.Column;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Map;

public class Student implements Serializer {

  private String fName;
  private String lName;
  private String street;
  private int zipcode;
  private String city;
  private String state;
  private String phone;
  private String email;
  private Timestamp visitedAt;
  private String fDate;
  public Student(String fName, String lName, String street, int zipcode, String city, String state, String phone, String email, Timestamp visitedAt) {
    this.fName = fName;
    this.lName = lName;
    this.street = street;
    this.zipcode = zipcode;
    this.city = city;
    this.state = state;
    this.phone = phone;
    this.email = email;
    this.visitedAt = visitedAt;
  }

  @Override
  public void configure(Map map, boolean b) {

  }

  @Override
  public byte[] serialize(String s, Object o) {
    return new byte[0];
  }

  @Override
  public byte[] serialize(String topic, Headers headers, Object data) {
    return new byte[0];
  }

  @Override
  public void close() {

  }
}
