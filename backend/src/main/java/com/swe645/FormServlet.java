package com.swe645;


import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/FormServlet")
public class FormServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    try {
      String jsonString = request.getReader().lines().collect(Collectors.joining());
      Gson gson = new Gson();
      StudentBean student = gson.fromJson(jsonString, StudentBean.class);
      Timestamp visited_at = Timestamp.valueOf(LocalDateTime.now());
      student.setVisitedAt(visited_at);
      // setting up the response config
      response.setContentType("application/json");
      response.setCharacterEncoding("UTF-8");
      // kafka operations
      StudentProducer.post(student);
      List<StudentBean> students = StudentConsumer.getAllSurveys();
      String res = gson.toJson(students);
      out.println(res);
    } catch (Exception e) {
      System.out.println("---------------err---------------");
      e.printStackTrace();
    } finally {
      out.close();
    }
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    try {
      Integer studentID = null;
      studentID = Integer.parseInt(request.getParameter("studentID"));
      StudentBean student = null;
      if (studentID != null)
        student = StudentConsumer.getById(studentID);
      String data = null;
      Gson gson = new Gson();
      if (student != null)
        data = gson.toJson(student);
      out.println(data);
    } catch (Exception e) {
      getServletContext().getRequestDispatcher("/404.html")
          .forward(request, response);
    } finally {
      out.close();
    }
  }
}
