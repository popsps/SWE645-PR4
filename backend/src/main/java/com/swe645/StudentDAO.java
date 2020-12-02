package com.swe645;

import javax.persistence.*;
import java.util.List;

//store and retrieve the Survey data into/from a database
// save  Student Survey Form data
// retrieve survey information from the database
public class StudentDAO {
  private static EntityManagerFactory entityManagerFactory
      = Persistence.createEntityManagerFactory("swe642db");

  // this function adds a new student to Student database.
  // It the student already exists it updates the student based on the new data
  public static void addStudent(StudentBean student) {
    EntityManager em = entityManagerFactory.createEntityManager();
    EntityTransaction et = null;
    StudentBean fStudent = null;
    try {
      et = em.getTransaction();
      et.begin();
      fStudent = em.find(StudentBean.class, student.getStudentID());
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

  // return a list of all students in student database
  public static List<StudentBean> getStudents() {
    EntityManager em = entityManagerFactory.createEntityManager();
    String query = "SELECT s FROM StudentBean s WHERE s.studentID IS NOT NULL";
    TypedQuery<StudentBean> tq = em.createQuery(query, StudentBean.class);
    List<StudentBean> students = null;
    try {
      students = tq.getResultList();

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      em.close();
      return students;
    }
  }

  // get a student based on its studentID
  public static StudentBean getStudent(int studentID) {
    EntityManager em = entityManagerFactory.createEntityManager();
    String query = "SELECT s FROM StudentBean s WHERE s.studentID = :id";
    TypedQuery<StudentBean> tq = em.createQuery(query, StudentBean.class);
    tq.setParameter("id", studentID);
    StudentBean s = null;
    try {
      s = tq.getSingleResult();
      System.out.println(s.getfName() + " " + s.getlName());
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      em.close();
      return s;
    }
  }

  // delete a student based on its studentID
  // return true on success
  public static boolean deleteStudent(int studentID) {
    EntityManager em = entityManagerFactory.createEntityManager();
    EntityTransaction et = null;
    StudentBean s = null;
    boolean success = false;
    try {
      et = em.getTransaction();
      et.begin();
      s = em.find(StudentBean.class, studentID);
      em.remove(s);
      et.commit();
      success = true;
    } catch (Exception e) {
      if (et != null)
        et.rollback();
      e.printStackTrace();
    } finally {
      em.close();
      return success;
    }
  }

  public static String getAllStudentsJson(List<StudentBean> students) {
    String data = "{\n\"students\": [\n";
    for (int i = 0; i < students.size(); i++) {
      data += "{ ";
      data += "\"fName\": " + "\"" + students.get(i).getfName() + "\", ";
      data += "\"lName\": " + "\"" + students.get(i).getlName() + "\"";
      if (i == students.size() - 1)
        data += " }\n";
      else
        data += " },\n";
    }
    data += "]\n}";
    return data;
  }

  public static String getStudentJson(StudentBean student) {
    String data = "{\n\"student\": {\n";
    data += "\"SID\": " + "\"" + student.getStudentID() + "\", ";
    data += "\"fName\": " + "\"" + student.getfName() + "\", ";
    data += "\"lName\": " + "\"" + student.getlName() + "\"";
    data += " }\n}";
    return data;
  }
}
