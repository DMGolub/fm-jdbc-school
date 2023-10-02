package com.ua.foxminded.dmgolub.school.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.ua.foxminded.dmgolub.school.domain.Course;
import com.ua.foxminded.dmgolub.school.domain.Student;

class InitialDataGeneratorTest {
    
    private ConnectionProvider connectionProvider = new TestConnectionProvider();
    
    @Nested
    class ConstructorIllegalArgumentExceptionTests {
        
        @Test
        void constructor_shouldThrowIllegalArgumentException_whenNumberOfGroupsIsNegative() {
            assertThrows(IllegalArgumentException.class, 
                () -> new InitialDataGenerator(-1, 1, 1, 1, connectionProvider));
        }
        
        @Test
        void constructor_shouldThrowIllegalArgumentException_whenNumberOfCoursesIsNegative() {
            assertThrows(IllegalArgumentException.class, 
                () -> new InitialDataGenerator(1, -1, 1, 1, connectionProvider));
        }
        
        @Test
        void constructor_shouldThrowIllegalArgumentException_whenNumberOfStudentsIsNegative() {
            assertThrows(IllegalArgumentException.class, 
                () -> new InitialDataGenerator(1, 1, -1, 1, connectionProvider));
        }
        
        @Test
        void constructor_shouldThrowIllegalArgumentException_whenNumberOfCoursesPerStudentIsNegative() {
            assertThrows(IllegalArgumentException.class, 
                () -> new InitialDataGenerator(1, 1, 1, -1, connectionProvider));
        }
        
        @Test
        void constructor_shouldThrowIllegalArgumentException_whenConnectionSupplierIsNull() {
            assertThrows(IllegalArgumentException.class, 
                () -> new InitialDataGenerator(1, 1, 1, 1, null));
        }
        
        @Test
        void constructor_shouldThrowIllegalArgumentException_whenRandomIsNull() {
            assertThrows(IllegalArgumentException.class, 
                () -> new InitialDataGenerator(1, 1, 1, 1, connectionProvider, null));
        }
    }
    
    @Nested
    class EmptyTableCreationTests {
        
        @Test
        void generator_shouldCreateAnEmptyTable_whenNumberOfGroupsIsZero() {
            final String tableName = "groups";
            try (
                Connection connection = connectionProvider.get();
                Statement statement = connection.createStatement();
            ) {
                statement.executeUpdate("DROP TABLE IF EXISTS " + tableName);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            
            InitialDataGenerator generator = new InitialDataGenerator(0, 0, 0, 0, connectionProvider);
            generator.generate();

            String resultTableName = null;
            String query = "SELECT * FROM " + tableName;
            int rowNumber = 0;
            try (
                Connection connection = connectionProvider.get();
                Statement statement = connection.createStatement()
            ) {
                DatabaseMetaData dbMetaData = connection.getMetaData();
                ResultSet tableResultSet = dbMetaData.getTables(null, null, tableName, null);
                while (tableResultSet.next()) {
                    resultTableName = tableResultSet.getString("TABLE_NAME");
                }
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    ++rowNumber;
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }

            assertTrue(resultTableName.equals(tableName) && rowNumber == 0);
        }
        
        @Test
        void generator_shouldCreateAnEmptyTable_whenNumberOfCoursesIsZero() {
            final String tableName = "courses";
            try (
                Connection connection = connectionProvider.get();
                Statement statement = connection.createStatement();
            ) {
                statement.executeUpdate("DROP TABLE IF EXISTS students_to_courses");
                statement.executeUpdate("DROP TABLE IF EXISTS " + tableName);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            
            InitialDataGenerator generator = new InitialDataGenerator(0, 0, 0, 0, connectionProvider);
            generator.generate();

            String resultTableName = null;
            String query = "SELECT * FROM " + tableName;
            int rowNumber = 0;
            try (
                Connection connection = connectionProvider.get();
                Statement statement = connection.createStatement()
            ) {
                DatabaseMetaData dbMetaData = connection.getMetaData();
                ResultSet tableResultSet = dbMetaData.getTables(null, null, tableName, null);
                while (tableResultSet.next()) {
                    resultTableName = tableResultSet.getString("TABLE_NAME");
                }
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    ++rowNumber;
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }

            assertTrue(resultTableName.equals(tableName) && rowNumber == 0);
        }
        
        @Test
        void generator_shouldCreateAnEmptyTable_whenNumberOfStudentsIsZero() {
            final String tableName = "students";
            try (
                Connection connection = connectionProvider.get();
                Statement statement = connection.createStatement();
            ) {
                statement.executeUpdate("DROP TABLE IF EXISTS students_to_courses");
                statement.executeUpdate("DROP TABLE IF EXISTS " + tableName);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            
            InitialDataGenerator generator = new InitialDataGenerator(0, 0, 0, 0, connectionProvider);
            generator.generate();

            String resultTableName = null;
            String query = "SELECT * FROM " + tableName;
            int rowNumber = 0;
            try (
                Connection connection = connectionProvider.get();
                Statement statement = connection.createStatement()
            ) {
                DatabaseMetaData dbMetaData = connection.getMetaData();
                ResultSet tableResultSet = dbMetaData.getTables(null, null, tableName, null);
                while (tableResultSet.next()) {
                    resultTableName = tableResultSet.getString("TABLE_NAME");
                }
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    ++rowNumber;
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }

            assertTrue(resultTableName.equals(tableName) && rowNumber == 0);
        }
        
        @Test
        void generator_shouldCreateAnEmptyTable_whenNumberOfCoursesPerStudentIsZero() {
            final String tableName = "students_to_courses";
            try (
                Connection connection = connectionProvider.get();
                Statement statement = connection.createStatement();
            ) {
                statement.executeUpdate("DROP TABLE IF EXISTS " + tableName);
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }
            
            InitialDataGenerator generator = new InitialDataGenerator(0, 0, 0, 0, connectionProvider);
            generator.generate();

            String resultTableName = null;
            String query = "SELECT * FROM " + tableName;
            int rowNumber = 0;
            try (
                Connection connection = connectionProvider.get();
                Statement statement = connection.createStatement()
            ) {
                DatabaseMetaData dbMetaData = connection.getMetaData();
                ResultSet tableResultSet = dbMetaData.getTables(null, null, tableName, null);
                while (tableResultSet.next()) {
                    resultTableName = tableResultSet.getString("TABLE_NAME");
                }
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    ++rowNumber;
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            }

            assertTrue(resultTableName.equals(tableName) && rowNumber == 0);
        }
    }
    
    @Test
    void generator_shouldAddFiveCourses_whenTheGivenNumberIsFiveAndTheFileContainsFiveCourses() {
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate("DROP TABLE IF EXISTS courses CASCADE");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        List<Course> expected = new ArrayList<>();
        expected.add(new Course(1, "Architecture", "Architecture test course"));
        expected.add(new Course(2, "Biology", "Biology test course"));
        expected.add(new Course(3, "Chemistry", "Chemistry test course"));
        expected.add(new Course(4, "Computer science", "Computer science test course"));
        expected.add(new Course(5, "Economics", "Economics test course"));
        
        InitialDataGenerator generator = new InitialDataGenerator(0, 5, 0, 0, connectionProvider);
        generator.generate();
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM courses";
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                courses.add(new Course(
                    resultSet.getInt(1), 
                    resultSet.getString(2), 
                    resultSet.getString(3)
                ));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        assertEquals(expected, courses);
    }
    
    @Test
    void generator_shouldCreateTenStudents_whenTheGivenNumberIsTenAndFilesContainEnoughNames() {
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate("DROP TABLE IF EXISTS students CASCADE");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        List<String> firstNames = Arrays.asList("Liam", "Noah", "Oliver", "Elijah", "William", 
            "James", "Benjamin", "Lucas", "Henry", "Alexander", "Olivia", "Emma", "Ava", 
            "Charlotte", "Sophia", "Amelia", "Isabella", "Mia", "Evelyn", "Harper");
        
        List<String> lastNames = Arrays.asList("Smith", "Johnson", "Williams", "Brown", "Jones", 
            "Miller", "Davis", "Garcia", "Rodrigues", "Wilson", "Martinez", "Anderson", "Taylor", 
            "Thomas", "Hernandez", "Moore", "Martin", "Jackson", "Thompson", "White");
                
        Random random = new Random();
        random.setSeed(1);
        List<Student> expected = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            random.nextInt(1);                  // group name is generated in generateStudents() before Students
            expected.add(new Student(
                i + 1, 
                null, 
                firstNames.get(random.nextInt(firstNames.size())), 
                lastNames.get(random.nextInt(lastNames.size()))
            ));
        }
        
        random.setSeed(1);
        InitialDataGenerator generator = new InitialDataGenerator(0, 0, 10, 0, connectionProvider, random);
        try {
            generator.generate();
        } catch(Exception e) {
            e.printStackTrace();
        }

        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM students";
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
        ) {
            while (resultSet.next()) {
                students.add(new Student(
                    resultSet.getInt("student_id"), 
                    null, 
                    resultSet.getString("first_name"), 
                    resultSet.getString("last_name")
                ));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        assertEquals(expected, students);
    }
    
    @Test
    void generator_shouldInsertTheGivenNumberOfUniqueGroupNames_whenALargeNumberIsGiven() {
        final String tableName = "groups";
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate("DROP TABLE IF EXISTS students_to_courses");
            statement.executeUpdate("DROP TABLE IF EXISTS " + tableName);
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        Set<String> groupNames = new HashSet<>();
        InitialDataGenerator generator = new InitialDataGenerator(500, 0, 0, 0, connectionProvider);
        generator.generate();
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT group_name FROM groups");
        ) {
            while(resultSet.next()) {
                groupNames.add(resultSet.getString("group_name"));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        
        assertEquals(500, groupNames.size());
    }
}