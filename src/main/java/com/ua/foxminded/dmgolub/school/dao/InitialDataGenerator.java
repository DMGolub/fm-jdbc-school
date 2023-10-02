package com.ua.foxminded.dmgolub.school.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.ua.foxminded.dmgolub.school.loader.FileLoader;
import com.ua.foxminded.dmgolub.school.loader.ResourceFileLoader;

public class InitialDataGenerator {
    
    private static final String DATABASE_SCHEMA = "databaseSchema.sql";
    
    private final int groupNumber;
    private final int courseNumber;
    private final int studentNumber;
    private final int maxCoursesPerStudent;
    ConnectionProvider connectionProvider;
    private Random random;
    
    public InitialDataGenerator(int groupNumber, int courseNumber, int studentNumber, 
        int maxCoursesPerStudent, ConnectionProvider connectionProvider) {
        this(groupNumber, courseNumber, studentNumber, maxCoursesPerStudent, 
            connectionProvider, new Random());
    }
    
    /**
     * Used for tests only. Allows to pass a Random instance with a predefined seed.
     * @param groupNumber
     * @param courseNumber
     * @param studentNumber
     * @param maxCoursesPerStudent
     * @param url
     * @param random
     */
    InitialDataGenerator(int groupNumber, int courseNumber, int studentNumber, 
        int maxCoursesPerStudent, ConnectionProvider connectionProvider, Random random) {
        if (groupNumber < 0) {
            throw new IllegalArgumentException("Number of groups can not be negative");
        }
        if (courseNumber < 0) {
            throw new IllegalArgumentException("Number of courses can not be negative");
        }
        if (studentNumber < 0) {
            throw new IllegalArgumentException("Number of students can not be negative");
        }
        if (maxCoursesPerStudent < 0) {
            throw new IllegalArgumentException("Number of courses per student can not be negative");
        }
        if (connectionProvider == null) {
            throw new IllegalArgumentException("Connection supplier can not be null");
        }
        if (random == null) {
            throw new IllegalArgumentException("Random can not be null");
        }
        this.groupNumber = groupNumber;
        this.courseNumber = courseNumber;
        this.studentNumber = studentNumber;
        this.maxCoursesPerStudent = maxCoursesPerStudent;
        this.connectionProvider = connectionProvider;
        this.random = random;
    }
    
    public void generate() {
        dropAllTables();
        createTables();
        generateGroups(groupNumber);
        generateStudents(studentNumber, groupNumber);
        generateCourses(courseNumber);
        generateStudentsToCourses(studentNumber, courseNumber, maxCoursesPerStudent);
    }
    
    private void dropAllTables() {
        dropTable("students_to_courses");
        dropTable("groups");
        dropTable("students");
        dropTable("courses");
    }
    
    private void dropTable(String tableName) {
        String query = "DROP TABLE IF EXISTS " + tableName;
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
    
    private void createTables() {
        FileLoader loader = new ResourceFileLoader(DATABASE_SCHEMA);
        List<String> queries = loader.load();
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            for (String query : queries) {
                statement.executeUpdate(query);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
    
    private void generateGroups(int groupNumber) {
        if (groupNumber < 0) {
            throw new IllegalArgumentException("Number of groups can not be negative");
        }
        if (groupNumber == 0) {
            return;
        }
        String query = "INSERT INTO groups(group_name) VALUES (?) ON CONFLICT DO NOTHING";
        try (
            Connection connection = connectionProvider.get();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            Set<String> groupNames = new HashSet<>();
            while (groupNames.size() < groupNumber) {
                groupNames.add(generateGroupName());
            }
            for (String groupName : groupNames) {
                statement.setString(1, groupName);
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
    
    private String generateGroupName() {
        final int alphabetSize = 26;
        StringBuilder result = new StringBuilder();
        result.append((char)(random.nextInt(alphabetSize) + 'A'))
            .append((char)(random.nextInt(alphabetSize) + 'A'))
            .append('-')
            .append(random.nextInt(10))
            .append(random.nextInt(10));
        return result.toString();
    }
    
    private void generateCourses(int courseNumber) {
        if (courseNumber < 0) {
            throw new IllegalArgumentException("Number of courses can not be negative");
        }
        if (courseNumber == 0) {
            return;
        }
        FileLoader loader = new ResourceFileLoader("courses.txt");        
        List<String> courseNames = loader.load();
        if (courseNames.size() < courseNumber) {
            throw new IllegalArgumentException("Courses number can not be greater than "
                + "courses name number: " + courseNames.size());
        }
        String query = "INSERT INTO courses(course_name, course_description) VALUES (?, ?)";
        try (
            Connection connection = connectionProvider.get();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            for (int i = 0; i < courseNumber; ++i) {
                statement.setString(1, courseNames.get(i));
                statement.setString(2, courseNames.get(i) + " test course");
                statement.executeUpdate();
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
    
    private void generateStudents(int studentNumber, int groupNumber) {
        if (studentNumber < 0) {
            throw new IllegalArgumentException("Number of students can not be negative");
        }
        if (studentNumber == 0) {
            return;
        }
        if (groupNumber < 0) {
            throw new IllegalArgumentException("Number of groups can not be negative");
        }
        FileLoader loader = new ResourceFileLoader("firstNames.txt");
        List<String> firstNames = loader.load();        
        loader = new ResourceFileLoader("lastNames.txt");
        List<String> lastNames = loader.load();
        String query = "INSERT INTO students (group_id, first_name, last_name) VALUES (?, ?, ?)";
        try (
            Connection connection = connectionProvider.get();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            for (int i = 0; i < studentNumber; ++i) {
                int groupId = random.nextInt(groupNumber + 1);
                if (groupId == 0) {
                    statement.setNull(1, Types.INTEGER);
                } else {
                    statement.setInt(1, groupId);
                }
                statement.setString(2, firstNames.get(random.nextInt(firstNames.size())));
                statement.setString(3, lastNames.get(random.nextInt(lastNames.size())));
                statement.executeUpdate();
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
    
    private void generateStudentsToCourses(int studentNumber, int courseNumber, int maxCoursesPerStudent) {
        if (studentNumber < 0) {
            throw new IllegalArgumentException("Number of students can not be negative");
        }
        if (courseNumber < 0) {
            throw new IllegalArgumentException("Number of students can not be negative");
        }
        if (maxCoursesPerStudent < 0) {
            throw new IllegalArgumentException("Number of courses can not be negative");
        }
        String query = "INSERT INTO students_to_courses (student_id, course_id) "
                + "VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (
            Connection connection = connectionProvider.get();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            for (int i = 0; i < studentNumber; ++i) {
                for (int j = 0; j < random.nextInt(maxCoursesPerStudent + 1); ++j) {
                    statement.setInt(1, random.nextInt(studentNumber) + 1);
                    statement.setInt(2, random.nextInt(courseNumber) + 1);
                    statement.executeUpdate();
                }
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }
}