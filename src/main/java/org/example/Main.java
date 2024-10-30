package org.example;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    // connect to the Sqlite database
    private static Connection connect() {
        String url = "jdbc:sqlite:datamergesample.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println(" Connection to Sqlite has been established");
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return  conn;
    }


    // create Students table
    public static void createStudentTable() {
        String sql = " create table if not exists students (\n"
                + " id integer primary key, \n"
                + " name text not null, \n"
                + " age integer check (age >= 5)\n"
                + ");";
        try(Connection conn = connect();
         Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println(" students table created successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // create Teachers table
    public static void createTeachersTable() {
        String sql = " create table if not exists teachers (\n"
                + " id  integer primary key, \n"
                + " name text not null, \n"
                + " subject text not null, \n"
                + " experience integer check( experience >= 0)\n"
                + ");";
        try(Connection conn = connect();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println(" Teachers table created successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // create coursesTable to link Students and Teachers with foreign Keys

    public static void createCourseTable() {
        String sql = " create table if not exists courses (\n"
                + " course_id integer primary key, \n"
                + " student_id integer not null, \n"
                + " teacher_id integer not null,\n"
                + " enrollment_date Text Default current_TimeStamp, \n"
                + " foreign key (student_id) references students (id) on delete cascade, \n"
                + " foreign key (teacher_id) references teachers (id) on delete set null \n"
                + ");";

        try(Connection conn = connect();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println(" Courses table created successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    // add data to the students table
    public static  void insertStudent(String name, int age) {
        String sql = " insert into students (name,age) values (?, ?)";
        try(Connection conn= connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.executeUpdate();
            System.out.println("student data has been added successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // add data to teachers
    public static  void insertTeacher(String name, String subject, int experience) {
        String sql = " insert into teachers (name,subject,experience) values (?, ?, ?)";
        try(Connection conn= connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, subject);
            pstmt.setInt(3, experience);
            pstmt.executeUpdate();
            System.out.println("Teacher data has been added successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Enroll a student in a teacher's course
    public  static void enrollStudentInCourse(int studentId, int teacherId)
    {
        String sql = " insert into courses (student_id, teacher_id) values (?,?)";
        try(Connection conn= connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId );
            pstmt.setInt(2, teacherId);
            pstmt.executeUpdate();
            System.out.println("student  has been enrolled successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // display all students

    public static void displayStudents() {
        String sql = "select * from students";
        try (Connection conn =connect(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println(" Students :");
            while(rs.next()) {
                System.out.println("Id : " + rs.getInt("id") + ", Name:  " + rs.getString("name") + " " +
                        ", Age : " + rs.getInt("age"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // display teachers
    public static void displayTeachers() {
        String sql = "select * from teachers";
        try (Connection conn =connect(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println(" Teachers :");
            while(rs.next()) {
                System.out.println("Id : " + rs.getInt("id") + ", Name:  " + rs.getString("name") + " " +
                        ", Subject : " + rs.getString("subject") + ", Experience : "
                        + rs.getInt("experience") + " years");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // display all enrolled courses

    public  static  void displayCourses() {
        String sql = "select courses.course_id, students.name As student_name, " +
                "teachers.name as teacher_name, courses.enrollment_date " +
                "from courses " +
                " join students on courses.student_id = students.id" +
                " join teachers on courses.teacher_id = teachers.id ";

        try (Connection conn = connect(); Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println(" Enrolled courses :");
            while (rs.next()) {
                System.out.println("Course id :" + rs.getInt("course_id") +
                        " , student : " + rs.getString("student_name") +
                        " , Teacher : " + rs.getString("teacher_name") +
                        ", Enrollment Date: " + rs.getString("enrollment_date")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        createStudentTable();
        createTeachersTable();
        createCourseTable();
        insertStudent("Adi",20);
        insertStudent("Nir",30);
        insertStudent("Bhava",27);

        insertTeacher("MR Smith", "Maths", 10);
        insertTeacher("MR John", "Science", 8);
        insertTeacher("MR Elon", "Physics", 11);
        enrollStudentInCourse(1,2);
        enrollStudentInCourse(2,1);

        displayStudents();
        displayTeachers();
        displayCourses();
    }
}