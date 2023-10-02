package com.ua.foxminded.dmgolub.school.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ua.foxminded.dmgolub.school.domain.Course;
import com.ua.foxminded.dmgolub.school.domain.Group;
import com.ua.foxminded.dmgolub.school.domain.Student;

public class StudentDAO {
    
    private ConnectionProvider connectionProvider;
    
    public StudentDAO(ConnectionProvider connectionProvider) {
        if (connectionProvider == null) {
            throw new IllegalArgumentException("Connection supplier can not be null");
        }
        this.connectionProvider = connectionProvider;
    }
    
    public int addNewStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student can not be null");
        }
        final String query = "INSERT INTO students (group_id, first_name, "
            + "last_name) VALUES (?, ?, ?)";
        int newId = 0;
        try (
            Connection connection = connectionProvider.get();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ) {
            statement.setInt(1, student.getGroup().getId());
            statement.setString(2, student.getFirstName());
            statement.setString(3, student.getLastName());
            int rowsAffected = statement.executeUpdate();
            ResultSet queryResult = statement.getGeneratedKeys();
            if (rowsAffected == 1) {
                queryResult.next();
                newId = queryResult.getInt(1);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return newId;
    }
    
    public boolean deleteStudentById(int studentId) {
        final String query = "DELETE FROM students WHERE student_id = ?";
        int result = 0;
        try (
            Connection connection = connectionProvider.get();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, studentId);
            result = statement.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return result == 1;
    }
    
    public Optional<Student> getStudentById(int studentId) {
        final String query = "SELECT student_id, first_name, last_name, "
            + "groups.group_id, group_name FROM students LEFT JOIN groups "
            + "ON students.group_id = groups.group_id WHERE student_id = ?";
        try (
            Connection connection = connectionProvider.get();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int groupId = resultSet.getInt("group_id");
                return Optional.of(new Student(
                    resultSet.getInt("student_id"),
                    groupId == 0 ? null : new Group(groupId, resultSet.getString("group_name")), 
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name")
                ));
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return Optional.empty();
    }
    
    public boolean addStudentToCourse(Student student, Course course) {
        if (student == null) {
            throw new IllegalArgumentException("Student can not be null");
        }
        if (course == null) {
            throw new IllegalArgumentException("Course can not be null");
        }
        final String query = "INSERT INTO students_to_courses(student_id, "
            + "course_id) VALUES(?, ?) ON CONFLICT DO NOTHING"; 
        int result = 0;
        try (
            Connection connection = connectionProvider.get();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, student.getId());
            statement.setInt(2, course.getId());
            result = statement.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return result == 1;
    }
    
    public boolean removeStudentFromCourse(Student student, Course course) {
        if (student == null) {
            throw new IllegalArgumentException("Student can not be null");
        }
        if (course == null) {
            throw new IllegalArgumentException("Course can not be null");
        }
        final String query = "DELETE FROM students_to_courses WHERE "
            + "student_id = ? AND course_id = ?";
        int result = 0;
        try (
            Connection connection = connectionProvider.get();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, student.getId());
            statement.setInt(2, course.getId());
            result = statement.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return result == 1;
    }
    
    public List<Course> getCoursesForStudent(int studentId) {
        final String query = "SELECT c.course_id, c.course_name, c.course_description "
            + "FROM courses AS c JOIN students_to_courses AS stc "
            + "ON c.course_id = stc.course_id WHERE student_id = ? ORDER BY course_id";
        List<Course> courses = new ArrayList<>();
        try (
            Connection connection = connectionProvider.get();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courses.add(new Course(
                    resultSet.getInt("course_id"), 
                    resultSet.getString("course_name"), 
                    resultSet.getString("course_description")
                ));
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return courses;
    }
    
    public List<Student> findStudentsByCourse(Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course can not be null");
        }        
        List<Student> students = new ArrayList<>();
        final String query = "SELECT s.student_id, g.group_id, g.group_name, "
            + "s.first_name, s.last_name FROM students AS s LEFT JOIN groups AS g "
            + "ON s.group_id = g.group_id JOIN students_to_courses AS stc "
            + "ON s.student_id = stc.student_id WHERE course_id = ?";
        try (
            Connection connection = connectionProvider.get();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, course.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int groupId = resultSet.getInt("group_id");
                students.add(new Student(
                    resultSet.getInt("student_id"), 
                    groupId == 0 ? null : new Group(groupId, resultSet.getString("group_name")), 
                    resultSet.getString("first_name"), 
                    resultSet.getString("last_name")
                ));
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return students;
    }
}