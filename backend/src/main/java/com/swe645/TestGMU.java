package com.swe645;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class TestGMU {
  private static EntityManagerFactory entityManagerFactory
      = Persistence.createEntityManagerFactory("swe642db");

  public static void main(String[] args) {
    Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
    System.out.println("now: " + timestamp);
    StudentBean student = new StudentBean(1232323, "Mike", "Doe", "Fairfax",
        22030, "fairfax", "VA", "202", "mike@gmail.com", timestamp);
    addStudent(student);
    getStudents();
    entityManagerFactory.close();
  }


  public static void addStudent(StudentBean student) {
    EntityManager em = entityManagerFactory.createEntityManager();
    EntityTransaction et = null;
    try {
      et = em.getTransaction();
      et.begin();
      StudentBean fStudent = em.find(StudentBean.class, student.getStudentID());
      if (fStudent != null)
        fStudent.clone(student);
      else
        fStudent = student;
      em.persist(fStudent);
      et.commit();
    } catch (Exception e) {
      if (et != null)
        et.rollback();
      e.printStackTrace();
    } finally {
      em.close();
    }
  }

  public static void getStudents() {
    EntityManager em = entityManagerFactory.createEntityManager();
    String query = "SELECT s FROM StudentBean s WHERE s.studentID IS NOT NULL";
    TypedQuery<StudentBean> tq = em.createQuery(query, StudentBean.class);
    List<StudentBean> students = null;
    try {
      students = tq.getResultList();
      students.forEach(student ->
          System.out.println(student.getStudentID() + " " + student.getfName() + " " + student.getlName() + " visited at: " + student.getVisitedAt()));
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      em.close();
    }
  }
}
