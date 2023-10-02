package com.ua.foxminded.dmgolub.school.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ua.foxminded.dmgolub.school.domain.Course;
import com.ua.foxminded.dmgolub.school.domain.Group;
import com.ua.foxminded.dmgolub.school.domain.Student;

class StudentDAOTest {
    
    private ConnectionProvider connectionProvider = new TestConnectionProvider();
    private StudentDAO studentDAO = new StudentDAO(connectionProvider);
    
    @BeforeEach
    void clearTableStudents() {
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate("DROP TABLE IF EXISTS students CASCADE");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS students("
                + "student_id SERIAL PRIMARY KEY, "
                + "group_id INTEGER, "
                + "first_name VARCHAR(50) NOT NULL, "
                + "last_name VARCHAR(50) NOT NULL"
                + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @BeforeEach
    void recreateTableStudentsToCourses() {
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate("DROP TABLE IF EXISTS students_to_courses");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS students_to_courses ("
                + "id SERIAL PRIMARY KEY, "
                + "student_id INTEGER NOT NULL, "
                + "course_id INTEGER NOT NULL, "
                + "CONSTRAINT \"uniqueRelation\" UNIQUE (\"student_id\", \"course_id\")"
                + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @BeforeEach
    void recreateTableGroups() {
        try (
                Connection connection = connectionProvider.get();
                Statement statement = connection.createStatement();
            ) {
                statement.executeUpdate("DROP TABLE IF EXISTS groups");
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS groups("
                    + "group_id SERIAL PRIMARY KEY, "
                    + "group_name VARCHAR(50) UNIQUE NOT NULL"
                    + ")");    
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    
    @Test
    void constructor_shouldThrowIllegalArgumentException_whenSupplierIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new StudentDAO(null));
    }
    
    @Test
    void addNewStudent_shouldThrowIllegalArgumentException_whenInputIsNull() {
        assertThrows(IllegalArgumentException.class, () -> studentDAO.addNewStudent(null));
    }
    
    @Test
    void addNewStudent_shouldAddNewStudentAndReturnNewId_whenCorrectArguemtntIsGiven() {
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate("DROP TABLE IF EXISTS students");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS students("
                + "student_id SERIAL PRIMARY KEY, "
                + "group_id INTEGER, "
                + "first_name VARCHAR(50) NOT NULL, "
                + "last_name VARCHAR(50) NOT NULL"
                + ")");
            statement.executeUpdate(String.format("INSERT INTO students "
                + "(group_id, first_name, last_name) VALUES (%d, \'%s\', \'%s\')",
                null, "firstName1", "lastName1"));
            statement.executeUpdate(String.format("INSERT INTO students "
                + "(group_id, first_name, last_name) VALUES (%d, \'%s\', \'%s\')",
                null, "firstName2", "lastName2"));
            statement.executeUpdate(String.format("INSERT INTO students "
                + "(group_id, first_name, last_name) VALUES (%d, \'%s\', \'%s\')",
                null, "firstName3", "lastName3"));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        Group group = new Group(1, "Group1");
        Student student = new Student(4, group, "firstName4", "lastName4");
        
        assertEquals(4, studentDAO.addNewStudent(student));
    }
    
    @Test
    void deleteStudent_shouldDeleteStudentAndReturnTrue_whenStudentExistsInTheDB() {
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(String.format(
                "INSERT INTO students(group_id, first_name, last_name) "
                + "VALUES (%d, \'%s\', \'%s\')", 1, "firstName1", "lastName1"));
            statement.executeUpdate(String.format(
                "INSERT INTO students(group_id, first_name, last_name) "
                + "VALUES (%d, \'%s\', \'%s\')", 2, "firstName2", "lastName2"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        assertTrue(studentDAO.deleteStudentById(1));
    }
    
    @Test
    void deleteStudent_shouldReturnFalse_whenStudentsDoesNotExistInTheDB() {
        assertTrue(!studentDAO.deleteStudentById(5));
    }

    @Test
    void getStudentById_shouldReturnExpectedStudent_whenStudentExistsInTheDB() {
        Group group = new Group(1, "Group1");
        Student expected = new Student(1, group, "firstName1", "lastName1");
        
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate("DROP TABLE IF EXISTS groups");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS groups("
                + "group_id SERIAL PRIMARY KEY, "
                + "group_name VARCHAR(50) UNIQUE NOT NULL"
                + ")");    
            statement.executeUpdate("INSERT INTO groups(group_name) VALUES (\'Group1\')"); 
            statement.executeUpdate(String.format(
                "INSERT INTO students(group_id, first_name, last_name) "
                + "VALUES (%d, \'%s\', \'%s\')", 1, "firstName1", "lastName1"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        assertEquals(expected, studentDAO.getStudentById(1).get());
    }
    
    @Test
    void getStudenById_shouldReturnEmptyOptional_whenStudentDoesNotExistInTheDB() {
        assertTrue(studentDAO.getStudentById(99).isEmpty());
    }
    
    @Test
    void addStudentToCourse_shouldThrowIllegalArgumentException_whenStudentIsNull() {
        Course course = new Course(1, "Course1", "TestCourse1");
        
        assertThrows(IllegalArgumentException.class, 
            () -> studentDAO.addStudentToCourse(null, course));
    }
    
    @Test
    void addStudentToCourse_shouldThrowIllegalArgumentException_whenCourseIsNull() {
        Student student = new Student(1, null, "firstName", "lastName");
        
        assertThrows(IllegalArgumentException.class, 
            () -> studentDAO.addStudentToCourse(student, null));
    }
    
    @Test
    void addStudentToCourse_shouldAddEntry_whenTheEntryHasNotBeenAddedYet() {
        Student student = new Student(1, null, "firstName", "lastName");
        Course course = new Course(1, "Course1", "TestCourse1");
        
        int counter = 0;
        studentDAO.addStudentToCourse(student, course);
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM students_to_courses");
        ) {
            while (resultSet.next()) {
                ++counter;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        assertEquals(1, counter);
    }
    
    @Test
    void addStudentToCourse_shoulNotAddEntry_whenTheEntryAlreadyExists() {
        Student student = new Student(1, null, "firstName", "lastName");
        Course course = new Course(1, "Course1", "TestCourse1");

        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(String.format(
                "INSERT INTO students_to_courses (student_id, course_id) "
                + " VALUES (%d, %d)", student.getId(), course.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        int counter = 0;
        studentDAO.addStudentToCourse(student, course);
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM students_to_courses");
        ) {
            while (resultSet.next()) {
                ++counter;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        assertEquals(1, counter);
    }
    
    @Test
    void removeStudentFromCourse_shouldThrowIllegalArgumentException_whenStudentIsNull() {
        Course course = new Course(1, "Course1", "TestCourse1");
        
        assertThrows(IllegalArgumentException.class,
            () -> studentDAO.removeStudentFromCourse(null, course));
    }
    
    @Test
    void removeStudentFromCourse_shouldThrowIllegalArgumentException_whenCourseIsNull() {
        Student student = new Student(1, null, "firstName", "lastName");
        
        assertThrows(IllegalArgumentException.class,
            () -> studentDAO.removeStudentFromCourse(student, null));
    }
    
    @Test
    void removeStudentFromCourse_shouldReturnFalse_whenTheTableIsEmpty() {
        Student student = new Student(1, null, "firstName", "lastName");
        Course course = new Course(1, "Course1", "TestCourse1");
        
        assertTrue(!studentDAO.removeStudentFromCourse(student, course));     
    }
    
    @Test
    void removeStudentFromCourse_shouldRemoveEntry_whenTheEntryAlreadyExists() {
        Student student = new Student(1, null, "firstName", "lastName");
        Course course = new Course(1, "Course1", "TestCourse1");

        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(String.format(
                "INSERT INTO students_to_courses (student_id, course_id) "
                + " VALUES (%d, %d)", student.getId(), course.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        int counter = 0;
        studentDAO.removeStudentFromCourse(student, course);
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT * FROM students_to_courses");
        ) {
            while (resultSet.next()) {
                ++counter;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        assertEquals(0, counter);
    }

    @Test
    void getCoursesForStudent_shouldReturnAnEmptyList_whenThereAreNoSuchEntries() {
        List<Course> expected = new ArrayList<>();
        
        assertEquals(expected, studentDAO.getCoursesForStudent(1));        
    }
    
    @Test
    void getCoursesForStudent_shouldReturnOneCourse_whenTheStudentHasOnlyOneCourse() {
        Student student = new Student(1, null, "firstName1", "lastName1");
        Course course = new Course(1, "Course1", "TestCourse1");
        List<Course> expected = new ArrayList<>();
        expected.add(course);
        
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate("DROP TABLE IF EXISTS courses");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS courses("
                + "course_id SERIAL PRIMARY KEY, "
                + "course_name VARCHAR(50), "
                + "course_description VARCHAR(50)"
                + ")");
            statement.executeUpdate("INSERT INTO courses (course_name, "
                 + "course_description) VALUES (\'Course1\', \'TestCourse1\')");
            statement.executeUpdate(String.format(
                "INSERT INTO students_to_courses (student_id, course_id) "
                + " VALUES (%d, %d)", student.getId(), course.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        assertEquals(expected, studentDAO.getCoursesForStudent(1));
    }
    
    @Test
    void findStudentsByCourse_shouldThrowIllegalArgumentException_whenInputIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> studentDAO.findStudentsByCourse(null));
    }
    
    @Test
    void findStudentsByCourse_shouldReturnAnEmptyList_whenThereAreNoSuchEntries() {
        Course course1 = new Course(1, "Course1", "TestCourse1");
        List<Student> expected = new ArrayList<>();
        
        Student student1 = new Student(1, null, "firstName1", "lastName1");
        Student student2 = new Student(2, null, "firstName1", "lastName1");
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(String.format(
                "INSERT INTO students_to_courses (student_id, course_id) "
                + " VALUES (%d, %d)", student1.getId(), course1.getId()));
            statement.executeUpdate(String.format(
                "INSERT INTO students_to_courses (student_id, course_id) "
                + " VALUES (%d, %d)", student2.getId(), course1.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Course course2 = new Course(2, "Course2", "TestCourse2");
        
        assertEquals(expected, studentDAO.findStudentsByCourse(course2));
    }
    
    @Test
    void findStudentsByCourse_shouldReturnOneStudent_whenThereIsOneSuchEntry() {
        Course course1 = new Course(1, "Course1", "TestCourse1");
        Student student1 = new Student(1, null, "firstName1", "lastName1");
        List<Student> expected = new ArrayList<>();
        expected.add(student1);
        
        Student student2 = new Student(2, null, "firstName1", "lastName1");
        Course course2 = new Course(2, "Course2", "TestCourse2");
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate("DROP TABLE IF EXISTS students");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS students("
                + "student_id SERIAL PRIMARY KEY, "
                + "group_id INTEGER, "
                + "first_name VARCHAR(50) NOT NULL, "
                + "last_name VARCHAR(50) NOT NULL"
                + ")");
            statement.executeUpdate(String.format("INSERT INTO students "
                + "(group_id, first_name, last_name) VALUES (%d, \'%s\', \'%s\')",
                null, "firstName1", "lastName1"));
            statement.executeUpdate(String.format(
                "INSERT INTO students_to_courses (student_id, course_id) "
                + " VALUES (%d, %d)", student1.getId(), course1.getId()));
            statement.executeUpdate(String.format(
                "INSERT INTO students_to_courses (student_id, course_id) "
                + " VALUES (%d, %d)", student2.getId(), course2.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        assertEquals(expected, studentDAO.findStudentsByCourse(course1));
    }
}