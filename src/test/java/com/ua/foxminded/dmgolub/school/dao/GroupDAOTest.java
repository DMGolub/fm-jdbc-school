package com.ua.foxminded.dmgolub.school.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ua.foxminded.dmgolub.school.domain.Group;

class GroupDAOTest {
    
    private ConnectionProvider connectionProvider = new TestConnectionProvider();
    private GroupDAO groupDAO = new GroupDAO(connectionProvider);
    
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
        assertThrows(IllegalArgumentException.class, () -> new GroupDAO(null));
    }

    @Test
    void getAllGroups_shouldReturnEmptyList_whenTableContainsNoEntries() {
        List<Group> expected = new ArrayList<>();
        
        assertEquals(expected, groupDAO.getAllGroups());
    }
    
    @Test
    void getAllGroups_shouldReturnThreeGroups_whenTableContainsThreeEntries() {
        List<Group> expected = new ArrayList<>();
        expected.add(new Group(1, "Group1"));
        expected.add(new Group(2, "Group2"));
        expected.add(new Group(3, "Group3"));

        final String query = "INSERT INTO groups(group_name) VALUES (?)";
        try (
            Connection connection = connectionProvider.get();
            PreparedStatement statement = connection.prepareStatement(query);
        ) {
            statement.setString(1, "Group1");
            statement.executeUpdate();
            statement.setString(1, "Group2");
            statement.executeUpdate();
            statement.setString(1, "Group3");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        assertEquals(expected, groupDAO.getAllGroups());
    }
    
    @Test
    void getGruopById_shouldReturnEmptyOptional_whenTableContainsNoGroupWithGivenId() {
        assertTrue(groupDAO.getGroupById(2).isEmpty());
    }
    
    @Test
    void getGroupById_shouldReturnTheCorrectGroup_whenTableContainsGroupWithGivenId() {
        Group expected = new Group(2, "Group2");
        
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(String.format(
                    "INSERT INTO groups(group_name) VALUES (\'%s\')", "Group1"));
                statement.executeUpdate(String.format(
                    "INSERT INTO groups(group_name) VALUES (\'%s\')", "Group2"));
                statement.executeUpdate(String.format(
                    "INSERT INTO groups(group_name) VALUES (\'%s\')", "Group3"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        assertEquals(expected, groupDAO.getGroupById(2).get());
    }

    @Test
    void findGroupsByStudentCount_shouldThrowIllegalArgumentException_whenInputIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> groupDAO.findGroupsByStudentCount(-1));
    }
    
    @Test
    void findGroupsByStudenCount_shoulReturnEmptyList_whenTableContainsNoEntries() {
        List<Group> expected = new ArrayList<>();
        
        assertEquals(expected, groupDAO.findGroupsByStudentCount(10));
    }
    
    @Test
    void findGroupsByStudenCount_shoulReturnEmptyList_whenGroupsSizesExceedTheGivenValue() {
        List<Group> expected = new ArrayList<>();
        
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
            statement.executeUpdate(String.format(
                    "INSERT INTO students(group_id, first_name, last_name) "
                    + "VALUES (%d, \'%s\', \'%s\')", 1, "firstName1", "lastName1"));
                statement.executeUpdate(String.format(
                    "INSERT INTO students(group_id, first_name, last_name) "
                    + "VALUES (%d, \'%s\', \'%s\')", 1, "firstName2", "lastName2"));
                statement.executeUpdate(String.format(
                    "INSERT INTO students(group_id, first_name, last_name) "
                    + "VALUES (%d, \'%s\', \'%s\')", 2, "firstName3", "lastName3"));
                statement.executeUpdate(String.format(
                    "INSERT INTO students(group_id, first_name, last_name) "
                    + "VALUES (%d, \'%s\', \'%s\')", 2, "firstName4", "lastName4"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        assertEquals(expected, groupDAO.findGroupsByStudentCount(1));
    }
    
    @Test
    void findGroupsByStudenCount_shoulReturnTwoGroups_whenTwoGroupsMeetTheGivenCondition() {
        List<Group> expected = new ArrayList<>();
        expected.add(new Group(1, "Group1"));
        expected.add(new Group(2, "Group2"));
        
        try (
            Connection connection = connectionProvider.get();
            Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(String.format(
                "INSERT INTO groups(group_name) VALUES (\'%s\')", "Group1"));
            statement.executeUpdate(String.format(
                "INSERT INTO groups(group_name) VALUES (\'%s\')", "Group2"));
            
            statement.executeUpdate("DROP TABLE IF EXISTS students CASCADE");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS students("
                + "student_id SERIAL PRIMARY KEY, "
                + "group_id INTEGER, "
                + "first_name VARCHAR(50) NOT NULL, "
                + "last_name VARCHAR(50) NOT NULL"
                + ")");
            statement.executeUpdate(String.format(
                "INSERT INTO students(group_id, first_name, last_name) "
                + "VALUES (%d, \'%s\', \'%s\')", 1, "firstName1", "lastName1"));
            statement.executeUpdate(String.format(
                "INSERT INTO students(group_id, first_name, last_name) "
                + "VALUES (%d, \'%s\', \'%s\')", 2, "firstName2", "lastName2"));
            statement.executeUpdate(String.format(
                "INSERT INTO students(group_id, first_name, last_name) "
                + "VALUES (%d, \'%s\', \'%s\')", 3, "firstName3", "lastName3"));
            statement.executeUpdate(String.format(
                "INSERT INTO students(group_id, first_name, last_name) "
                + "VALUES (%d, \'%s\', \'%s\')", 3, "firstName4", "lastName4"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        assertEquals(expected, groupDAO.findGroupsByStudentCount(1));
    }
}