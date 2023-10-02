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

public class CourseDAO {
    
    private ConnectionProvider connectionProvider;
    
    public CourseDAO(ConnectionProvider connectionProvider) {
        if (connectionProvider == null) {
            throw new IllegalArgumentException("Connection supplier can not be null");
        }
        this.connectionProvider = connectionProvider;
    }
    
    public List<Course> getAllCourses() {
        final String query = "SELECT course_id, course_name, course_description FROM courses";
        List<Course> courses = new ArrayList<>();
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
        ) {
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
    
    public Optional<Course> getCourseById(int courseId) {
        final String query = "SELECT course_id, course_name, course_description "
            + "FROM courses WHERE course_id = ?";
        try (
            Connection connection = connectionProvider.get();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setInt(1, courseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Course(
                    resultSet.getInt("course_id"), 
                    resultSet.getString("course_name"), 
                    resultSet.getString("course_description")
                ));
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return Optional.empty();
    }
    
    public Optional<Course> getCourseByName(String courseName) {
        if (courseName == null) {
            throw new IllegalArgumentException("Course name can not be null");
        }
        if (courseName.isEmpty()) {
            throw new IllegalArgumentException("Course name can not be an empty string");
        }
        final String query = "SELECT course_id, course_name, course_description "
            + "FROM courses WHERE course_name = ?";
        try (
            Connection connection = connectionProvider.get();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, courseName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Course(
                    resultSet.getInt("course_id"), 
                    resultSet.getString("course_name"), 
                    resultSet.getString("course_description")
                ));
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return Optional.empty();
    }
}