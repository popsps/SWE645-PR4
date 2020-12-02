package com.swe645;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class testJson {
  public static void main(String[] args) {
    String data = "{\n\"students\":[\n";
    StudentBean[] students = new StudentBean[5];
    for (StudentBean student : students) {
      Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
      student = new StudentBean(1232323, "Mike", "Doe", "Fairfax",
          22030, "fairfax", "VA", "202", "mike@gmail.com", timestamp);
      data += "{ ";
      data += "\"fName\": " + "\"" + student.getfName() + "\", ";
      data += "\"lName\": " + "\"" + student.getlName() + "\"";
      data += " },\n";
    }
    data += "{ ";
    data += "\"fName\": " + "\"" + "Goo" + "\", ";
    data += "\"lName\": " + "\"" + "Ravioli" + "\"";
    data += " }\n";
    data += "]\n}";
    System.out.println(data);
  }
}
