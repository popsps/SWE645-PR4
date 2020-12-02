package com.swe645;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "student")
public class StudentBean implements Serializable {
  //    com.swe642.StudentBean has attributes that matches most
  //    of the Student Survey Form fields, except the Data field

  @Id
  @Column(name = "id", unique = true)
  private int studentID;
  @Column(name = "firstname", nullable = false)
  private String fName;
  @Column(name = "lastname", nullable = false)
  private String lName;
  @Column(name = "street")
  private String street;
  @Column(name = "zipcode")
  private int zipcode;
  @Column(name = "city")
  private String city;
  @Column(name = "state")
  private String state;
  @Column(name = "phone")
  private String phone;
  @Column(name = "email")
  private String email;
  @Column(name = "visited_at")
  private Timestamp visitedAt;
  @Column(name = "fdate")
  private String fDate;


  @Column(name = "recommend")
  private String recommend;
  @Column(name = "howknow")
  private String howknow;
  @Column(name = "comments")
  private String comments;

  @Column(name = "_students")
  private boolean _students;
  @Column(name = "_location")
  private boolean _location;
  @Column(name = "_campus")
  private boolean _campus;
  @Column(name = "_atmosphere")
  private boolean _atmosphere;
  @Column(name = "_dormrooms")
  private boolean _dormrooms;
  @Column(name = "_sports")
  private boolean _sports;

  public StudentBean(int studentID, String fName, String lName, String street, int zipcode, String city, String state, String phone, String email, Timestamp visitedAt, String fDate, String recommend, String howknow, String comments, boolean _students, boolean _location, boolean _campus, boolean _atmosphere, boolean _dormrooms, boolean _sports) {
    this.studentID = studentID;
    this.fName = fName;
    this.lName = lName;
    this.street = street;
    this.zipcode = zipcode;
    this.city = city;
    this.state = state;
    this.phone = phone;
    this.email = email;
    this.visitedAt = visitedAt;
    this.fDate = fDate;
    this.recommend = recommend;
    this.howknow = howknow;
    this.comments = comments;
    this._students = _students;
    this._location = _location;
    this._campus = _campus;
    this._atmosphere = _atmosphere;
    this._dormrooms = _dormrooms;
    this._sports = _sports;
  }

  public boolean is_students() {
    return _students;
  }

  public void set_students(boolean _students) {
    this._students = _students;
  }

  public boolean is_location() {
    return _location;
  }

  public void set_location(boolean _location) {
    this._location = _location;
  }

  public boolean is_campus() {
    return _campus;
  }

  public void set_campus(boolean _campus) {
    this._campus = _campus;
  }

  public boolean is_atmosphere() {
    return _atmosphere;
  }

  public void set_atmosphere(boolean _atmosphere) {
    this._atmosphere = _atmosphere;
  }

  public boolean is_dormrooms() {
    return _dormrooms;
  }

  public void set_dormrooms(boolean _dormrooms) {
    this._dormrooms = _dormrooms;
  }

  public boolean is_sports() {
    return _sports;
  }

  public void set_sports(boolean _sports) {
    this._sports = _sports;
  }

  public String getRecommend() {
    return recommend;
  }

  public void setRecommend(String recommend) {
    this.recommend = recommend;
  }

  public String getHowknow() {
    return howknow;
  }

  public void setHowknow(String howknow) {
    this.howknow = howknow;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public StudentBean(int studentID, String fName, String lName, String street, int zipcode, String city, String state, String phone, String email, Timestamp visitedAt) {
    this.studentID = studentID;
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

  public StudentBean(int studentID, String fName, String lName, String street, int zipcode, String city, String state, String phone, String email) {
    this.studentID = studentID;
    this.fName = fName;
    this.lName = lName;
    this.street = street;
    this.zipcode = zipcode;
    this.city = city;
    this.state = state;
    this.phone = phone;
    this.email = email;
    this.visitedAt = null;
  }

  public StudentBean(int studentID, String fName, String lName, String street, int zipcode, String city, String state, String phone, String email, String fDate, Timestamp visitedAt) {
    this.studentID = studentID;
    this.fName = fName;
    this.lName = lName;
    this.street = street;
    this.zipcode = zipcode;
    this.city = city;
    this.state = state;
    this.phone = phone;
    this.email = email;
    this.fDate = fDate;
    this.visitedAt = visitedAt;
  }

  public StudentBean() {
    this.studentID = 0;
    this.fName = "";
    this.lName = "";
    this.street = "";
    this.zipcode = 0;
    this.city = "";
    this.state = "";
    this.phone = "";
    this.email = "";
    this.fDate = "";
  }

  public void clone(StudentBean newStudent) {
    this.studentID = newStudent.getStudentID();
    this.fName = newStudent.getfName();
    this.lName = newStudent.getlName();
    this.street = newStudent.getStreet();
    this.zipcode = newStudent.getZipcode();
    this.city = newStudent.getCity();
    this.state = newStudent.getState();
    this.phone = newStudent.getPhone();
    this.email = newStudent.getEmail();
    this.visitedAt = newStudent.getVisitedAt();
  }

  public int getStudentID() {
    return studentID;
  }

  public void setStudentID(int studentID) {
    this.studentID = studentID;
  }

  public String getfName() {
    return fName;
  }

  public void setfName(String fName) {
    this.fName = fName;
  }

  public String getlName() {
    return lName;
  }

  public void setlName(String lName) {
    this.lName = lName;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public int getZipcode() {
    return zipcode;
  }

  public void setZipcode(int zipCode) {
    this.zipcode = zipCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Timestamp getVisitedAt() {
    return visitedAt;
  }

  public void setVisitedAt(Timestamp visitedAt) {
    this.visitedAt = visitedAt;
  }

  public String getfDate() {
    return fDate;
  }

  public void setfDate(String fDate) {
    this.fDate = fDate;
  }
}
