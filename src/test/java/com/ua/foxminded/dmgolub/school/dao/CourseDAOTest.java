package com.ua.foxminded.dmgolub.school.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ua.foxminded.dmgolub.school.domain.Course;

class CourseDAOTest {
    
    private ConnectionProvider connectionProvider = new TestConnectionProvider();
    private CourseDAO courseDAO = new CourseDAO(connectionProvider);
    
    @BeforeEach
    private void prepareTableCourses() {
        recreateTableCourses();
        initiateTableCourses();
    }
    
    private void recreateTableCourses() {
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate("DROP TABLE IF EXISTS courses CASCADE");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS courses("
                + "course_id SERIAL PRIMARY KEY, "
                + "course_name VARCHAR(50), "
                + "course_description VARCHAR(50)"
                + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void initiateTableCourses() {
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(String.format(
                "INSERT INTO courses(course_name, course_description) "
                + "VALUES (\'%s\', \'%s\')", "Course1", "TestCourse1"));
            statement.executeUpdate(String.format(
                "INSERT INTO courses(course_name, course_description) "
                + "VALUES (\'%s\', \'%s\')", "Course2", "TestCourse2"));
            statement.executeUpdate(String.format(
                "INSERT INTO courses(course_name, course_description) "
                + "VALUES (\'%s\', \'%s\')", "Course3", "TestCourse3"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    void constructor_shouldThrowIllegalArgumentException_whenSupplierIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new CourseDAO(null));
    }
    
    @Test
    void getAllCourses_shouldReturnEmptyList_whenTableContainsNoEntries() {
        List<Course> expected = new ArrayList<>();
        
        recreateTableCourses();
        
        assertEquals(expected, courseDAO.getAllCourses());
    }
    
    @Test
    void getAllCourses_shouldReturnThreeCourses_whenTableContainsThreeEntries() {
        List<Course> expected = new ArrayList<>();
        expected.add(new Course(1, "Course1", "TestCourse1"));
        expected.add(new Course(2, "Course2", "TestCourse2"));
        expected.add(new Course(3, "Course3", "TestCourse3"));

        assertEquals(expected, courseDAO.getAllCourses());
    }
    
    @Test
    void getCourseById_shouldReturnEmptyOptional_whenTableDoesNotContainACourseWithGivenId() {
        assertTrue(courseDAO.getCourseById(4).isEmpty());
    }
    
    @Test
    void getCourseById_shouldReturnTheCorrectCourse_whenTableContainsCourseWithGivenId() {
        Course expected = new Course(2, "Course2", "TestCourse2");
        
        assertEquals(expected, courseDAO.getCourseById(2).get());
    }
    
    @Test
    void getCourseByName_shouldThrowIllegalArgumentException_whenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> courseDAO.getCourseByName(null));
    }
    
    @Test
    void getCourseByName_shouldThrowIllegalArgumentException_whenInputIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> courseDAO.getCourseByName(""));
    }
    
    @Test
    void getCourseByName_shouldReturnEmptyOptional_whenThereIsNoCourseWithTheGivenName() {
        assertTrue(courseDAO.getCourseByName("Course4").isEmpty());
    }
    
    @Test
    void getCourseByName_shouldReturnTheCorrectCourse_whenTableContainsCourseWithGivenName() {
        Course expected = new Course(3, "Course3", "TestCourse3");
        
        assertEquals(expected, courseDAO.getCourseByName("Course3").get());
    }
}